package com.example.map.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.map.domain.Comment

class CommentsDiffUtilItemCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}