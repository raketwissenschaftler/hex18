package com.treecio.hexplore

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.treecio.hexplore.ble.BLEService
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    override fun onStart() {
        super.onStart()

        btnBroadcast.setOnClickListener {
            val intent = Intent(this, BLEService::class.java)
            intent.putExtra(BLEService.EXTRA_MODE, BLEService.MODE_BROADCAST)
            startService(intent)
        }
        btnBroadcast.setOnClickListener {
            val intent = Intent(this, BLEService::class.java)
            intent.putExtra(BLEService.EXTRA_MODE, BLEService.MODE_DISCOVERY)
            startService(intent)
        }
    }

}
