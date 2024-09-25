package com.example.map.di

import androidx.lifecycle.ViewModel
import com.example.map.data.MapRepositoryImpl
import com.example.map.domain.MapRepository
import com.example.map.presentation.viewmodels.DetailPhotoInfoViewModel
import com.example.map.presentation.viewmodels.LoginViewModel
import com.example.map.presentation.viewmodels.PhotosViewModel
import com.example.map.presentation.viewmodels.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface DomainModule {
    @Binds
    fun bindMapRepository(mapRepositoryImpl: MapRepositoryImpl): MapRepository

    @IntoMap
    @ViewModelKey(DetailPhotoInfoViewModel::class)
    @Binds
    fun bindsDetailPhotoInfoViewModel(detailPhotoInfoViewModel: DetailPhotoInfoViewModel): ViewModel

    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    @Binds
    fun bindsLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @IntoMap
    @ViewModelKey(PhotosViewModel::class)
    @Binds
    fun bindsPhotosViewModel(photosViewModel: PhotosViewModel): ViewModel

    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    @Binds
    fun bindsRegistrationViewModel(registrationViewModel: RegistrationViewModel): ViewModel

}