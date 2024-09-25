package com.example.map.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.map.databinding.FragmentPhotosBinding
import com.example.map.presentation.viewmodels.PhotosViewModel
import com.example.map.presentation.viewmodels.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import javax.inject.Inject


class PhotosFragment : Fragment() {
    private lateinit var binding: FragmentPhotosBinding
    private lateinit var photosAdapter: PhotosAdapter
    private lateinit var accessToken: String

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var page = 0

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private val photosViewModel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[PhotosViewModel::class]
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as Bitmap
                    getLocationAndEncodeImage(imageBitmap)
                }
            }
        accessToken =
            arguments?.getString(KEY_ACCESS_KEY) ?: throw IllegalStateException("Токен не пришел")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            photosAdapter = PhotosAdapter(it)
        }
        val gridLayoutManager = GridLayoutManager(context, 3)
        with(binding.rvListOfPhotoCards) {
            layoutManager = gridLayoutManager
            adapter = photosAdapter
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        addAdapterListeners()

        binding.fAB.setOnClickListener {
            if (checkPermissions()) {
                requestPermission()
            } else {
                checkLocation()
            }
        }
        photosViewModel.loadPhotos(accessToken, page)
        addLoadingPhotosListener()
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CAMERA_AND_LOCATION
        )

    }

    private fun addAdapterListeners() {
        photosAdapter.onExtraLoad = {
            page = it
            photosViewModel.loadPhotos(accessToken, page)
        }
        photosAdapter.onLongClick = {
            photosViewModel.deleteOnePhoto(accessToken, it, page)
        }
        photosAdapter.loadDetailFragment = {
            val intent = OnePhotoActivity.newIntent(requireContext(), it, accessToken)
            startActivity(intent)
        }
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_AND_LOCATION && grantResults.isNotEmpty()) {
            val cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val locationPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED
            if (cameraPermission && locationPermission) {
                checkLocation()
            }
        } else {
            Log.d("PhotosFragment", "Отказано в доступе к камере либо локации")
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            openCamera()
        } else {
            DialogManager.locationSettingsDialog(requireContext()) {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcher.launch(takePictureIntent)
    }

    private fun isLocationEnabled(): Boolean {
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLocationAndEncodeImage(bitmap: Bitmap) {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val lat = it.latitude
                val lng = it.longitude
                Log.d("PhotosFragment", "$it")
                photosViewModel.postPhoto(bitmap, lat, lng, accessToken)
            }
        }
    }

    private fun addLoadingPhotosListener() {
        photosViewModel.photosScreenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PhotosScreenState.SuccessLoading -> {
                    photosAdapter.submitList(state.photoList)
                }

                PhotosScreenState.Loading -> {}
                is PhotosScreenState.SuccessDeleting -> Toast.makeText(
                    requireContext(),
                    state.status,
                    Toast.LENGTH_SHORT
                ).show()

                is PhotosScreenState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    companion object {
        private const val REQUEST_CAMERA_AND_LOCATION = 100
        private const val KEY_ACCESS_KEY = "key_access_key"
        fun newFragment(accessToken: String): PhotosFragment {
            return PhotosFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ACCESS_KEY, accessToken)
                }
            }
        }
    }
}