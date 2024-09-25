package com.example.map.data.network.dto.userDto

import com.google.gson.annotations.SerializedName

data class RegistrationData(
    @SerializedName("UserId")
    val id: Int,
    @SerializedName("login")
    val login: String,
    @SerializedName("token")
    val token: String
)