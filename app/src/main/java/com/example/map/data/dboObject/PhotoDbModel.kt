package com.example.map.data.dboObject

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "photos_table")
data class PhotoDbModel(
    @PrimaryKey
    val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double,
)