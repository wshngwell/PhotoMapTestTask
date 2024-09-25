package com.example.map.presentation

import com.example.map.domain.Comment
import com.example.map.domain.Photo

sealed class DetailPhotoInfoScreenState {
    object Error:DetailPhotoInfoScreenState()
    data class LoadedPhotos (val photo: Photo ):DetailPhotoInfoScreenState()
    data class LoadedComments (val comments :List<Comment> ):DetailPhotoInfoScreenState()
}