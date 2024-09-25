package com.example.map.domain

import javax.inject.Inject

class PostPostCardUseCase @Inject constructor(private val repository: MapRepository) {

    suspend operator fun invoke(postCard: Photo, accessToken: String) {
        repository.postImage(postCard, accessToken)
    }
}