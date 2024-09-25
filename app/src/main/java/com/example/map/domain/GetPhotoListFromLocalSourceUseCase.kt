package com.example.map.domain

import javax.inject.Inject

class GetPhotoListFromLocalSourceUseCase @Inject constructor(private val repository: MapRepository) {


    suspend operator fun invoke(accessToken: String): List<Photo> {
        return repository.getPhotosListFromDb(accessToken)

    }

}