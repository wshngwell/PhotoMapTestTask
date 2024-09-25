package com.example.map.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.map.data.dboObject.CommentDbModel
import com.example.map.data.dboObject.PhotoDbModel
import java.util.ArrayList

@Dao
interface PhotosDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhotosToDb(list: List<PhotoDbModel>)

    @Query("SELECT * FROM photos_table WHERE id=:id")
    suspend fun getOneCoinFullInfo(id: Int): PhotoDbModel

    @Query("SELECT * FROM PHOTOS_TABLE ORDER BY id DESC")
    suspend fun loadPhotosFromDb(): List<PhotoDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCommentsToDb(list: List<CommentDbModel>)

    @Query("SELECT * FROM comments_table ORDER BY id DESC")
    suspend fun loadCommentsFromDb(): List<CommentDbModel>

    @Query("DELETE FROM comments_table ")
    suspend fun deleteCommentsFromDb()

    @Query("DELETE FROM photos_table ")
    suspend fun deletePhotosFromDb()

    @Query("DELETE FROM photos_table WHERE id=:imageId ")
    suspend fun deletePhotoFromDb(imageId: Int)

}