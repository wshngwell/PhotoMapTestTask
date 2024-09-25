package com.example.map.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.map.R
import com.example.map.databinding.FragmentMapsBinding
import com.example.map.domain.Photo
import com.example.map.presentation.viewmodels.PhotosViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.ArrayList
import kotlin.coroutines.coroutineContext

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var accessToken: String
    private lateinit var photos: java.util.ArrayList<Photo>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (requireActivity().application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accessToken = arguments?.getString(ACCESS_KEY) ?: throw IllegalStateException("no token")
        photos = arguments?.getParcelableArrayList(LIST_PHOTOS) ?: throw IllegalStateException()
        initializeMap()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        photos.forEach {
            val coordinate = LatLng(it.lat, it.lng)
            googleMap.addMarker(
                MarkerOptions()
                    .position(coordinate)
                    .title("Marker")
            )
        }

    }

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


    }

    companion object {
        private const val ACCESS_KEY = "Access_key"
        private const val LIST_PHOTOS = "photos_list"
        fun newMapsFragment(accessKey: String, listOfPhotos: ArrayList<Photo>): MapsFragment {
            return MapsFragment().apply {
                arguments = Bundle().apply {
                    putString(ACCESS_KEY, accessKey)
                    putParcelableArrayList(LIST_PHOTOS, listOfPhotos)
                }
            }
        }
    }
}