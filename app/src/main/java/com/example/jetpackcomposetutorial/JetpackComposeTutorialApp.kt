package com.example.jetpackcomposetutorial

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class JetpackComposeTutorialApp : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        // Initialize WorkManager
        WorkManager.getInstance(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
} 