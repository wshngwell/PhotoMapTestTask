package com.example.map.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: Int = -1,
    val src: String,
    val date: Long,
    val lat: Double,
    val lng: Double,
) : Parcelable