package com.example.map.presentation

import android.app.Application
import com.example.map.di.DaggerApplicationComponent

class MapApp : Application() {


    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}