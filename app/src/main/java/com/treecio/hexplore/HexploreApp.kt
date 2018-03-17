package com.treecio.hexplore

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import timber.log.Timber

class HexploreApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber
        Timber.plant(Timber.DebugTree())

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}
