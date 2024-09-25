package com.example.map.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.map.databinding.ActivityOnePhotoBinding
import com.example.map.presentation.viewmodels.DetailPhotoInfoViewModel
import com.example.map.presentation.viewmodels.ViewModelFactory
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class OnePhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnePhotoBinding
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var accessToken: String
    private val photoId: Int by lazy {
        intent.getIntExtra(KEY_PHOTO_ID, 0)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val detailPhotoInfoViewModel by lazy {
        ViewModelProvider.create(this, viewModelFactory)[DetailPhotoInfoViewModel::class]
    }

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MapApp).component.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityOnePhotoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        commentsAdapter = CommentsAdapter()
        accessToken = intent.getStringExtra(
            KEY_ACCESS_KEY
        ) ?: throw IllegalStateException("No access_key")

        detailPhotoInfoViewModel.getCommentsList(
            accessToken, photoId, page
        )
        detailPhotoInfoViewModel.loadOnePhoto(photoId)
        addViewModelListeners()
        addAdapterListeners()
    }

    override fun onResume() {
        super.onResume()
        detailPhotoInfoViewModel.deleteCommentsFromDb()
    }

    private fun addAdapterListeners() {
        commentsAdapter.onExtraLoad = {
            page = it
            detailPhotoInfoViewModel.getCommentsList(
                accessToken, photoId, page
            )
        }
    }

    private fun addViewModelListeners() {
        detailPhotoInfoViewModel.detailPhotosScreenState.observe(this) { state ->
            when (state) {
                DetailPhotoInfoScreenState.Error -> {


                }

                is DetailPhotoInfoScreenState.LoadedPhotos -> {

                    Glide.with(this)
                        .load(state.photo.src)
                        .into(binding.onePhoto)
                    binding.arrowBack.setOnClickListener {
                        finish()
                    }
                    binding.dateOfPhoto.text = convertTimestampToDate(state.photo.date)
                    binding.sendImageButton.setOnClickListener {
                        val textMessage = binding.ediTextMessage.text
                            ?: throw IllegalStateException("No access_key")

                        detailPhotoInfoViewModel.postComment(
                            accessToken,
                            textMessage.toString(),
                            photoId,
                            page
                        )
                        binding.ediTextMessage.setText("")

                    }
                }

                is DetailPhotoInfoScreenState.LoadedComments -> {

                    binding.commentsRV.adapter = commentsAdapter
                    commentsAdapter.submitList(state.comments)
                }
            }
        }

    }

    private fun convertTimestampToDate(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }


    companion object {
        private const val KEY_PHOTO_ID = "KEY_PHOTO_ID"
        private const val KEY_ACCESS_KEY = "key_access_key"
        fun newIntent(context: Context, id: Int, accessToken: String): Intent {
            return Intent(context, OnePhotoActivity::class.java).apply {
                putExtra(KEY_PHOTO_ID, id)
                putExtra(KEY_ACCESS_KEY, accessToken)
            }
        }
    }
}