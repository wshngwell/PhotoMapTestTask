package com.example.map.data

import android.app.Application
import android.util.Log
import com.example.map.data.database.PhotosDao
import com.example.map.data.network.ApiService
import com.example.map.domain.Comment
import com.example.map.domain.MapRepository
import com.example.map.domain.Photo
import com.example.map.domain.RegistrationUserResult
import com.example.map.domain.User
import javax.inject.Inject

class MapRepositoryImpl @Inject constructor(
    val application: Application,
    private val mapper: Mapper,
    private val dao: PhotosDao,
    private val apiService: ApiService
) : MapRepository {

    override suspend fun login(user: User): RegistrationUserResult {
        val userDto = mapper.mapUserToUserDto(user)
        val answer = apiService.login(userDto)
        return mapper.mapRegistrationAnswerDtoToRegistrationUserResult(answer)
    }

    override suspend fun resister(user: User): RegistrationUserResult {
        val userDto = mapper.mapUserToUserDto(user)
        val answer = apiService.register(userDto)
        return mapper.mapRegistrationAnswerDtoToRegistrationUserResult(answer)
    }

    override suspend fun postImage(photo: Photo, accessToken: String) {
        val photoDto = mapper.mapPhotoCardToPhotoDto(photo)
        apiService.postImage(
            photoInDto = photoDto,
            accessToken = accessToken
        )

    }

    override suspend fun getListOfImages(accessToken: String, page: Int): List<Photo> {
        val photosAnswer = apiService.getPhotos(accessToken, page)
        Log.d("list", photosAnswer.toString())
        val photosDbModelList = photosAnswer.data.map {
            mapper.maoPhotoOutDtoToPhotoDbModel(it)
        }
        dao.addPhotosToDb(photosDbModelList)
        val photos = dao.loadPhotosFromDb().map {
            mapper.maoPhotoDbModelToPhoto(it)
        }
        Log.d("list", photos.toString())
        Log.d("MapRepositoryImpl", photos.toString())
        return photos
    }

    override suspend fun getOneImage(id: Int): Photo {
        val photoDbModel = dao.getOneCoinFullInfo(id)
        return mapper.maoPhotoDbModelToPhoto(photoDbModel)
    }

    override suspend fun postComment(accessToken: String, comment: Comment, imageId: Int) {
        val commentDtoIn = mapper.mapCommentToCommentDtoIn(comment)

        apiService.postComment(accessToken, commentDtoIn, imageId)
    }

    override suspend fun getCommentsListFromNetwork(
        accessToken: String,
        imageId: Int,
        page: Int
    ): List<Comment> {
        val commentsAnswer = apiService.getCommentsList(accessToken, imageId, page)
        val commentsDbModelList = commentsAnswer.data.map {
            mapper.mapCommentDtoOutToCommentDbModel(it)
        }
        dao.addCommentsToDb(commentsDbModelList)
        val commentsDbModelListFromDataBase = dao.loadCommentsFromDb()
        return commentsDbModelListFromDataBase.map {
            mapper.mapCommentDbModelToComment(it)
        }
    }

    override suspend fun deleteCommentsFromDb() {
        dao.deleteCommentsFromDb()
    }

    override suspend fun deletePhotosFromDb() {
        dao.deletePhotosFromDb()
    }

    override suspend fun deletePhotos(accessToken: String, imageId: Int): String {
        val deletingResponse = apiService.deletePhoto(accessToken, imageId)
        dao.deletePhotoFromDb(imageId)
        return deletingResponse.status.toString()
    }

    override suspend fun getPhotosListFromDb(accessToken: String): List<Photo> {
        return dao.loadPhotosFromDb().map {
            mapper.maoPhotoDbModelToPhoto(it)
        }

    }
}