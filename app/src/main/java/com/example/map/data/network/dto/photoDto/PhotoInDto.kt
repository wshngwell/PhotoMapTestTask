package com.example.map.data.network.dto.photoDto

import com.google.gson.annotations.SerializedName

data class PhotoInDto(
   @SerializedName("base64Image")
    val base64Image: String,
   @SerializedName("date")
    val date: Int,
   @SerializedName("lat")
    val lat: Double,
   @SerializedName("lng")
    val lng: Double,
)