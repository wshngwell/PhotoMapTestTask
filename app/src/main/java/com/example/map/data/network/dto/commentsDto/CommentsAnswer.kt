package com.example.map.data.network.dto.commentsDto

import com.google.gson.annotations.SerializedName

class CommentsAnswer(
    @SerializedName("data")
    val data: List<CommentDtoOut>
)