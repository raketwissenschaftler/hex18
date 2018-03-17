package com.treecio.hexplore.db

import android.content.Context
import com.raizlabs.android.dbflow.config.DatabaseConfig
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
import com.raizlabs.android.dbflow.runtime.DirectModelNotifier

/**
 * Initializes the DBFlow library.
 */
object DBFlowInit {

    fun init(context: Context) {
        FlowManager.init(FlowConfig.builder(context)
                .addDatabaseConfig(DatabaseConfig.builder(AppDatabase::class.java)
                        .databaseName(AppDatabase.NAME)
                        .modelNotifier(DirectModelNotifier.get())
                        .build()
                )
                .build()
        )
    }

}
