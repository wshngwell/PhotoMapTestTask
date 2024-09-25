package com.example.map.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.map.domain.Photo

class PhotosDiffUtilItemCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}