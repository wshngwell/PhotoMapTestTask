package com.example.map.data.network.dto.photoDto

import com.google.gson.annotations.SerializedName

data class PhotosAnswer (
    @SerializedName("data")
    val data:List<PhotoOutDto>

)