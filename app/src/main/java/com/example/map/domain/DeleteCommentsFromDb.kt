package com.example.map.domain

import javax.inject.Inject

class DeleteCommentsFromDb @Inject constructor(private val repository: MapRepository) {
    suspend operator fun invoke() {
        repository.deleteCommentsFromDb()
    }
}