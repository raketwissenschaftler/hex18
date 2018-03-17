package com.treecio.hexplore.ble

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.ParcelUuid
import com.treecio.hexplore.R
import com.treecio.hexplore.utils.toBytes
import timber.log.Timber
import java.util.*


class BLEService : Service() {

    companion object {
        const val EXTRA_MODE = "extra_mode"
        const val MODE_BROADCAST = "mode_broadcast"
        const val MODE_DISCOVERY = "mode_discovery"
    }

    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var bluetoothLeAdvertiser: BluetoothLeAdvertiser

    val mHandler = Handler()
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("Starting service")

        when (intent?.getStringExtra(EXTRA_MODE)) {
            MODE_BROADCAST -> startBroadcasting()
            MODE_DISCOVERY -> startDiscovery()
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
        bluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
    }

    override fun onDestroy() {
        stopDiscovery()
        stopBroadcasting()
        super.onDestroy()
    }

    val advertisingCallback = object : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
            Timber.i("Advertise success!")
        }

        override fun onStartFailure(errorCode: Int) {
            Timber.e("Advertising onStartFailure: $errorCode")
        }
    }

    private fun startBroadcasting() {
        val settings = AdvertiseSettings.Builder()
                .setAdvertiseMode(BleConfig.ADVERTISE_MODE)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(false)
                .build()

        val pUuid = ParcelUuid(getServiceUuid())

        val data = AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .setIncludeTxPowerLevel(false)
                .addServiceUuid(pUuid)
                .addServiceData(pUuid, getDeviceIDBytes())
                .build()

        bluetoothLeAdvertiser.startAdvertising(settings, data, advertisingCallback)
    }

    private fun stopBroadcasting() {
        bluetoothLeAdvertiser.stopAdvertising(advertisingCallback)
    }

    val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.let { onSingleResult(it) }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            results.forEach(::onSingleResult)
        }

        fun onSingleResult(result: ScanResult) {
            result.device ?: return
            result.scanRecord.serviceData.values.forEach { remoteDeviceId ->
                Timber.i("Scanned ${remoteDeviceId.asList()}")
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Timber.e("Discovery onScanFailed: $errorCode")
        }
    }

    private fun startDiscovery() {
        val filters = listOf(
                ScanFilter.Builder()
                        .setServiceUuid(ParcelUuid(getServiceUuid()))
                        .build()
        )
        val settings = ScanSettings.Builder().setScanMode(BleConfig.SCAN_MODE).build()

        bluetoothLeScanner.startScan(filters, settings, scanCallback)
    }

    private fun stopDiscovery() {
        bluetoothLeScanner.stopScan(scanCallback);
    }

    private fun getServiceUuid() = UUID.fromString(getString(R.string.ble_service_uuid))
    private fun getDeviceIDBytes() = UUID.randomUUID()
            .toBytes().take(BleConfig.MAX_BYTES).toByteArray()

}
