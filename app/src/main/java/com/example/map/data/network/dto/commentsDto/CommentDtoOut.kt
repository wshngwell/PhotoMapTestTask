package com.example.map.data.network.dto.commentsDto

import com.google.gson.annotations.SerializedName

data class CommentDtoOut(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: Int,
    @SerializedName("text")
    val text: String,
)