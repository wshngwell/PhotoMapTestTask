package com.example.map.presentation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.map.databinding.PhotoCardBinding
import com.example.map.domain.Photo
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class PhotosAdapter(val context: Context) :
    ListAdapter<Photo, PhotosViewHolder>(PhotosDiffUtilItemCallback()) {

    var onExtraLoad: ((Int) -> Unit)? = null
    var loadDetailFragment: ((Int) -> Unit)? = null
    var onLongClick: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {

        val view = PhotoCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PhotosViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = currentList[position]


        val photoContainer = holder.binding.photoSrc

        Glide.with(context)
            .load(photo.src)
            .into(photoContainer)

        holder.binding.root.setOnClickListener {
            loadDetailFragment?.invoke(photo.id)
        }

        holder.binding.root.setOnLongClickListener {
            Log.d("PhotosAdapter", "${photo.id}")
            onLongClick?.invoke(photo.id)
            true

        }

        holder.binding.dateOfPhoto.text = convertTimestampToDate(photo.date)

        if ((position == currentList.size - 5) && onExtraLoad != null) {
            page++
            onExtraLoad?.invoke(page)
        }
    }

    companion object {
        private var page = 0
    }
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

