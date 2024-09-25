package com.example.map.domain

import com.google.gson.annotations.SerializedName

data class Comment(
    val id: Int = 0,
    val date: Int = 0,
    val text: String,
)