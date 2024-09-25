package com.example.map.data.network.dto.commentsDto

import com.google.gson.annotations.SerializedName

data class CommentDtoIn (
    @SerializedName("text")
    val text:String
)