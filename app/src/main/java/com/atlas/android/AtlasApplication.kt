package com.atlas.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AtlasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
