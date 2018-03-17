package com.treecio.hexplore.ble

import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanSettings

object BleConfig {

    const val ADVERTISE_MODE = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY
    const val SCAN_MODE = ScanSettings.SCAN_MODE_LOW_LATENCY

    const val MAX_BYTES = 9

}
