package com.example.map.domain

import javax.inject.Inject

class RefreshPhotosUseCase @Inject constructor(private val repository: MapRepository) {
    suspend operator fun invoke() {
        repository.deletePhotosFromDb()
    }
}