package com.example.map.domain

import javax.inject.Inject

class GetListOfPhotosUseCase @Inject constructor(private val repository: MapRepository) {

    suspend operator fun invoke(accessToken: String, page: Int): List<Photo> {
        return repository.getListOfImages(accessToken, page)
    }
}