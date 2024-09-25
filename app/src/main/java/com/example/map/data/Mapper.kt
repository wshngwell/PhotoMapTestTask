package com.example.map.data

import com.example.map.data.dboObject.CommentDbModel
import com.example.map.data.dboObject.PhotoDbModel
import com.example.map.data.network.dto.commentsDto.CommentDtoIn
import com.example.map.data.network.dto.commentsDto.CommentDtoOut
import com.example.map.data.network.dto.photoDto.PhotoInDto
import com.example.map.data.network.dto.photoDto.PhotoOutDto
import com.example.map.data.network.dto.userDto.RegistrationAnswer
import com.example.map.data.network.dto.userDto.UserDto
import com.example.map.domain.Comment
import com.example.map.domain.Photo
import com.example.map.domain.RegistrationUserResult
import com.example.map.domain.User
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapRegistrationAnswerDtoToRegistrationUserResult(registrationAnswer: RegistrationAnswer):
            RegistrationUserResult {
        return RegistrationUserResult(
            login = registrationAnswer.data.login,
            token = registrationAnswer.data.token
        )
    }

    fun mapUserToUserDto(user: User): UserDto {
        return UserDto(login = user.login, password = user.password)
    }

    fun mapPhotoCardToPhotoDto(photo: Photo): PhotoInDto {
        return PhotoInDto(
            base64Image = photo.src,
            date = photo.date.toInt(),
            lat = photo.lat,
            lng = photo.lng
        )
    }

    fun maoPhotoOutDtoToPhotoDbModel(photoOutDto: PhotoOutDto): PhotoDbModel {
        return PhotoDbModel(
            id = photoOutDto.id,
            url = photoOutDto.url,
            date = photoOutDto.date,
            lat = photoOutDto.lat,
            lng = photoOutDto.lng
        )
    }

    fun maoPhotoDbModelToPhoto(photoDbModel: PhotoDbModel): Photo {
        return Photo(
            id = photoDbModel.id,
            src = photoDbModel.url,
            date = photoDbModel.date,
            lat = photoDbModel.lat,
            lng = photoDbModel.lng
        )
    }

    fun mapCommentToCommentDtoIn(comment: Comment): CommentDtoIn {
        return CommentDtoIn(text = comment.text)
    }

    fun mapCommentDtoOutToCommentDbModel(commentDtoOut: CommentDtoOut): CommentDbModel {
        return CommentDbModel(
            id = commentDtoOut.id,
            date = commentDtoOut.date,
            text = commentDtoOut.text
        )
    }

    fun mapCommentDbModelToComment(commentDbModel: CommentDbModel): Comment {
        return Comment(
            id = commentDbModel.id,
            date = commentDbModel.date,
            text = commentDbModel.text
        )
    }

}