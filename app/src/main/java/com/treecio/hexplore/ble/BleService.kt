package com.treecio.hexplore.ble

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber


class BleService : Service() {

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val MODE_BROADCAST = "mode_broadcast"
        const val MODE_DISCOVERY = "mode_discovery"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Starting service")

        val state = when (intent?.getStringExtra(EXTRA_MODE)) {
            MODE_BROADCAST -> BleBroadcasting(this)
            MODE_DISCOVERY -> BleDiscovery(this)
            else -> return START_STICKY
        }

        state.prepare()
        state.transitionIn()

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

}
