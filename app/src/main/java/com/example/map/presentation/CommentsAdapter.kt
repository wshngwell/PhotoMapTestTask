package com.example.map.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.map.databinding.CommentCardBinding
import com.example.map.domain.Comment
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CommentsAdapter : ListAdapter<Comment, CommentsViewHolder>(CommentsDiffUtilItemCallback()) {

    var onExtraLoad: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {

        val view = CommentCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = currentList[position]
        holder.binding.commentText.text = comment.text
        holder.binding.timeOfComment.text = convertTimestampToDate(comment.date.toLong())
        if ((position == currentList.size - 5) && onExtraLoad != null) {
           page++
            onExtraLoad?.invoke(page)
        }

    }

    companion object {
        private var page = 0
    }
    private fun convertTimestampToDate(timestamp: Long?): String {
        if (timestamp == null) return ""
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val pattern = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)

    }

}