package com.example.map.data.network.dto.userDto

import com.google.gson.annotations.SerializedName

data class RegistrationAnswer(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data : RegistrationData
)