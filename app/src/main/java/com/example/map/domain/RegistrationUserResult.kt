package com.example.map.domain

import com.google.gson.annotations.SerializedName

data class RegistrationUserResult(
    val login: String,
    val token: String
)
