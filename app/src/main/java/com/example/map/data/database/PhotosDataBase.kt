package com.example.map.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.map.data.dboObject.CommentDbModel
import com.example.map.data.dboObject.PhotoDbModel

@Database(
    entities = [PhotoDbModel::class, CommentDbModel::class],
    version = 3,
    exportSchema = false
)
abstract class PhotosDataBase : RoomDatabase() {

    abstract fun getPhotosDao(): PhotosDao

    companion object {
        private val LOCK = Any()
        private const val PHOTOS_DB = "PHOTOS_DB"
        private var photosDataBase: PhotosDataBase? = null

        fun createPhotoDataBase(application: Application): PhotosDataBase {
            photosDataBase?.let {
                return it
            }
            synchronized(LOCK) {
                photosDataBase?.let {
                    return it
                }
                val instance =
                    Room.databaseBuilder(application, PhotosDataBase::class.java, PHOTOS_DB)
                        .build()
                photosDataBase = instance
                return instance

            }
        }
    }

}