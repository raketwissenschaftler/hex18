package com.treecio.hexplore.ble

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Context
import android.os.ParcelUuid
import timber.log.Timber

class BleDiscovery(context: Context) : BleAbstractState(context) {

    private lateinit var bluetoothLeScanner: BluetoothLeScanner

    private val scanCallback = object : ScanCallback() {
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

    override fun prepare() {
        bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    }


    override fun transitionIn() = startDiscovery()
    override fun transitionOut() = stopDiscovery()
}
