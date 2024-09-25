package com.example.map.data.network.dto.photoDto

import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    @SerializedName("status")
    val status :Int
)