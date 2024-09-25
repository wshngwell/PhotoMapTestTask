package com.example.map.presentation

import com.example.map.domain.Photo

sealed class PhotosScreenState {
    class SuccessLoading(val photoList :List<Photo>) : PhotosScreenState()
    class Error (val message:String):PhotosScreenState()
    object Loading :PhotosScreenState()
    class SuccessDeleting(val status:String) :PhotosScreenState()
}