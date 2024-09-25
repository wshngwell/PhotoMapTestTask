package com.example.map.domain

import com.example.map.data.network.MapRepositoryImpl
import javax.inject.Inject

data class PostCommentUseCase @Inject constructor(private val repository: MapRepository) {
    suspend operator fun invoke(accessToken: String, comment: Comment, imageId: Int) {
        repository.postComment(accessToken, comment, imageId)
    }
}