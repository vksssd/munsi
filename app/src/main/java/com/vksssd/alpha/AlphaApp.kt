package com.vksssd.alpha

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlphaApp: Application(), Configuration.Provider  {

    override fun onCreate() {
        super.onCreate()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        TODO("Not yet implemented")
    }
}