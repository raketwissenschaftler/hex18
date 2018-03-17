package com.treecio.hexplore.model

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Table
import com.treecio.hexplore.db.AppDatabase
import java.util.*

@Table(database = AppDatabase::class)
class User (

        @Column var shortId: ByteArray? = null,

        @Column var handshakeCount: Int = 0,
        @Column var lastHandshake: Date? = null

)
