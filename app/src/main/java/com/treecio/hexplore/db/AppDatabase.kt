package com.treecio.hexplore.db

import com.raizlabs.android.dbflow.annotation.Database

@Database(
        version = AppDatabase.VERSION,
        foreignKeyConstraintsEnforced = true
)
object AppDatabase {

    const val VERSION = 1

    val NAME = AppDatabase::class.simpleName

}
