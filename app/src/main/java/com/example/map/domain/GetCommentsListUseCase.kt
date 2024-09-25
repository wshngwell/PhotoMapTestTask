package com.example.map.domain

import javax.inject.Inject

class GetCommentsListUseCase @Inject constructor(private val repository: MapRepository) {

    suspend operator fun invoke(
        accessToken: String,
        imageId: Int,
        page: Int
    ): List<Comment> {
        return repository.getCommentsListFromNetwork(accessToken, imageId, page)
    }
}