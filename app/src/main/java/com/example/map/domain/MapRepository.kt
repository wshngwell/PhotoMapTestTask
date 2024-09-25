package com.example.map.domain

import java.util.ArrayList

interface MapRepository {
    suspend fun login(user: User): RegistrationUserResult
    suspend fun resister(user: User): RegistrationUserResult
    suspend fun postImage(photo: Photo, accessToken: String)
    suspend fun getOneImage(id: Int): Photo

    suspend fun getListOfImages(accessToken: String, page: Int): List<Photo>

    suspend fun postComment(
        accessToken: String,
        comment: Comment,
        imageId: Int
    )

    suspend fun getCommentsListFromNetwork(
        accessToken: String,
        imageId: Int,
        page: Int
    ): List<Comment>

    suspend fun getPhotosListFromDb(accessToken: String, ): List<Photo>

    suspend fun deleteCommentsFromDb()
    suspend fun deletePhotosFromDb()
    suspend fun deletePhotos(accessToken: String, imageId: Int): String
}