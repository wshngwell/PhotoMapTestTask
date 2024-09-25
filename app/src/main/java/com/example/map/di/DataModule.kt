package com.example.map.di

import android.app.Application
import com.example.map.data.database.PhotosDao
import com.example.map.data.database.PhotosDataBase
import com.example.map.data.network.ApiFactory
import com.example.map.data.network.ApiService
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    companion object {
        @Provides
        fun providesDao(application: Application): PhotosDao {
            return PhotosDataBase.createPhotoDataBase(application).getPhotosDao()
        }

        @Provides
        fun providesApiService(): ApiService {
            return ApiFactory.apiService
        }
    }
}