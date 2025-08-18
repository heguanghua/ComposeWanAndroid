package com.compose.wan.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WanApplication : Application() {
    companion object {
        lateinit var instance: WanApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}