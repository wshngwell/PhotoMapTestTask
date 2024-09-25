package com.example.map.domain

import javax.inject.Inject

class GetOnePhotoInfoUseCase @Inject constructor(private val repository: MapRepository) {
    suspend operator fun invoke(id:Int) :Photo {
       return repository.getOneImage(id)
    }
}