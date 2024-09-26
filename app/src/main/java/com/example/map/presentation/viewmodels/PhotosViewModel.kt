package com.example.map.presentation.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.map.domain.DeletePhotoUseCase
import com.example.map.domain.GetListOfPhotosUseCase
import com.example.map.domain.GetPhotoListFromLocalSourceUseCase
import com.example.map.domain.Photo
import com.example.map.domain.PostPostCardUseCase
import com.example.map.domain.RefreshPhotosUseCase
import com.example.map.presentation.PhotosScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.Date
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
    private val postPostCardUseCase: PostPostCardUseCase,
    private val getListOfPhotosUseCase: GetListOfPhotosUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val refreshPhotosUseCase: RefreshPhotosUseCase,
    private val getPhotosFromLocalSource: GetPhotoListFromLocalSourceUseCase
) : ViewModel() {


    init {
        viewModelScope.launch {
            refreshPhotosUseCase()
        }
    }

    private val _photosScreenState = MutableLiveData<PhotosScreenState>()

    val photosScreenState: MutableLiveData<PhotosScreenState>
        get() = _photosScreenState


    private val coroutineExceptionHandlerForPostingPhoto =
        CoroutineExceptionHandler { _, throwable ->
            Log.d("PhotosViewModel", throwable.message.toString())
        }
    private val coroutineExceptionHandlerForLoadingPhotos =
        CoroutineExceptionHandler { _, throwable ->
            Log.d("PhotosViewModel", throwable.message.toString())
        }
    private val coroutineExceptionHandlerForDeletingPhotos =
        CoroutineExceptionHandler { _, throwable ->
            _photosScreenState.value = PhotosScreenState.Error(throwable.message.toString())
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postPhoto(bitmap: Bitmap, lat: Double, lng: Double, accessToken: String) {
        val isLoading = _photosScreenState.value;
        if (isLoading != null && isLoading == PhotosScreenState.Loading) {
            return;
        }
        val photo = Photo(
            src = encodeImageToBase64(bitmap),
            date = Date().time / 1000,
            lat = lat,
            lng = lng
        )

        viewModelScope.launch(coroutineExceptionHandlerForPostingPhoto) {
            postPostCardUseCase(photo, accessToken)
            loadPhotos(accessToken, 0)
        }

    }

    fun loadPhotos(accessToken: String, page: Int) {
        viewModelScope.launch(coroutineExceptionHandlerForLoadingPhotos) {
            val photoList = getListOfPhotosUseCase(accessToken, page)

            _photosScreenState.value = PhotosScreenState.SuccessLoading(photoList)
        }
    }

    fun deleteOnePhoto(accessToken: String, imageId: Int, page: Int) {
        viewModelScope.launch(coroutineExceptionHandlerForDeletingPhotos) {
            val status = deletePhotoUseCase(accessToken, imageId)
            _photosScreenState.value = PhotosScreenState.SuccessDeleting(status)
            loadPhotos(accessToken, page)
        }
    }

    fun getPhotosFromDb(accessToken: String) {
        viewModelScope.launch {
            val list = getPhotosFromLocalSource(accessToken)
            _photosScreenState.value = PhotosScreenState.SuccessLoading(list)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun encodeImageToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.getEncoder().encodeToString(byteArray)
    }


}