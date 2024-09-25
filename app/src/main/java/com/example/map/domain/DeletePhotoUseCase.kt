package com.example.map.domain

import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(private val repository: MapRepository) {

    suspend operator fun invoke(accessToken: String, imageID: Int): String {
        return repository.deletePhotos(accessToken, imageID)
    }
}