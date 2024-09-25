package com.example.map.data.network.dto.photoDto

import com.google.gson.annotations.SerializedName

data class PhotoOutDto (

    @SerializedName("id")
    val id :Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("date")
    val date: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
)