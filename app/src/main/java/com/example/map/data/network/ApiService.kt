package com.example.map.data.network

import com.example.map.data.network.dto.commentsDto.CommentDtoIn
import com.example.map.data.network.dto.commentsDto.CommentsAnswer
import com.example.map.data.network.dto.photoDto.DeleteResponse
import com.example.map.data.network.dto.photoDto.PhotoInDto
import com.example.map.data.network.dto.photoDto.PhotosAnswer
import com.example.map.data.network.dto.userDto.RegistrationAnswer
import com.example.map.data.network.dto.userDto.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("api/account/signup")
    suspend fun register(@Body userDto: UserDto): RegistrationAnswer

    @POST("api/account/signin")
    suspend fun login(@Body userDto: UserDto): RegistrationAnswer


    @POST("api/image")
    suspend fun postImage(
        @Body photoInDto: PhotoInDto,
        @Header(ACCESS_TOKEN) accessToken: String,
    )

    @GET("api/image")
    suspend fun getPhotos(
        @Header(ACCESS_TOKEN) accessToken: String,
        @Query(PAGE) page: Int
    ): PhotosAnswer

    @POST("/api/image/{imageId}/comment")
    suspend fun postComment(
        @Header(ACCESS_TOKEN) accessToken: String,
        @Body commentDtoIn: CommentDtoIn,
        @Path(IMAGE_ID) imageId: Int
    )

    @GET("api/image/{imageId}/comment")
    suspend fun getCommentsList(
        @Header(ACCESS_TOKEN) accessToken: String,
        @Path(IMAGE_ID) imageId: Int,
        @Query(PAGE) page: Int
    ): CommentsAnswer

    @DELETE("api/image/{id}")
    suspend fun deletePhoto(
        @Header(ACCESS_TOKEN) accessToken: String,
        @Path(ID) imageId: Int,
    ): DeleteResponse


    companion object {
        private const val ACCESS_TOKEN = "Access-Token"
        private const val PAGE = "page"
        private const val ID = "id"
        private const val IMAGE_ID = "imageId"
    }
}