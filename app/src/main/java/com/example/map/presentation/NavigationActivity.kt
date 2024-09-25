package com.example.map.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.map.R
import com.example.map.databinding.ActivityNavigationBinding
import com.example.map.domain.Photo
import com.example.map.presentation.viewmodels.PhotosViewModel
import com.example.map.presentation.viewmodels.ViewModelFactory
import java.util.ArrayList
import javax.inject.Inject

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var menuButton: ImageButton
    private lateinit var userName: TextView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val photosViewModel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[PhotosViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuButton = findViewById(R.id.navigationButton)

        val accessToken = intent.getStringExtra(ACCESS_TOKEN_KEY) ?: ""
        openPhotosFragment(accessToken)

        val headerView = binding.navigationView.getHeaderView(0)
        userName = headerView.findViewById(R.id.userName)
        userName.text = intent.getStringExtra(USER_NAME_KEY)


        menuButton.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.navPhotos) {
                openPhotosFragment(accessToken)

            } else if (it.itemId == R.id.navMap) {
                openMapsFragment(accessToken)

            }
            binding.drawerLayout.close()
            false

        }

    }

    private fun openPhotosFragment(accessToken: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container_view_photos_or_map,
                PhotosFragment.newFragment(accessToken)
            ).commit()
    }

    private fun openMapsFragment(accessToken: String) {
        photosViewModel.getPhotosFromDb(accessToken)
        photosViewModel.photosScreenState.observe(this) {
            when (it) {
                is PhotosScreenState.Error -> {}
                PhotosScreenState.Loading -> {

                }

                is PhotosScreenState.SuccessDeleting -> {}
                is PhotosScreenState.SuccessLoading -> {
                    val list = it.photoList
                    val arrayList = ArrayList<Photo>()
                    list.forEach { photo ->
                        arrayList.add(photo)
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fragment_container_view_photos_or_map,
                            MapsFragment.newMapsFragment(accessToken, arrayList)
                        ).commit()
                }
            }
        }

    }

    companion object {
        private const val USER_NAME_KEY = "user_name"
        private const val ACCESS_TOKEN_KEY = "token"
        fun newIntent(context: Context, userName: String, accessToken: String): Intent {
            return Intent(context, NavigationActivity::class.java).apply {
                putExtra(USER_NAME_KEY, userName)
                putExtra(ACCESS_TOKEN_KEY, accessToken)
            }
        }
    }
}