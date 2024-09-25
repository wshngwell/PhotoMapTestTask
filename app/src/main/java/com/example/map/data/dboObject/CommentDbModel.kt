package com.example.map.data.dboObject

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "comments_table")
data class CommentDbModel(
    @PrimaryKey
    val id: Int,
    val date: Int,
    val text: String,
)