package com.example.map.di

import android.app.Application
import com.example.map.presentation.AuthorizationActivity
import com.example.map.presentation.LoginFragment
import com.example.map.presentation.MapApp
import com.example.map.presentation.MapsFragment
import com.example.map.presentation.NavigationActivity
import com.example.map.presentation.OnePhotoActivity
import com.example.map.presentation.PhotosFragment
import com.example.map.presentation.RegisterFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DomainModule::class, DataModule::class])
interface ApplicationComponent {

    fun inject(coinApp: MapApp)
    fun inject(registrationFragment: RegisterFragment)
    fun inject(onePhotoActivity: OnePhotoActivity)
    fun inject(navigationActivity: NavigationActivity)
    fun inject(authorizationActivity: AuthorizationActivity)
    fun inject(loginFragment: LoginFragment)
    fun inject(mapsFragment: MapsFragment)
    fun inject(photosFragment: PhotosFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}