package com.treecio.hexplore.utils

import java.nio.ByteBuffer
import java.util.*


fun ByteArray.toUUID(): UUID {
    val bb = ByteBuffer.wrap(this)
    val firstLong = bb.getLong()
    val secondLong = bb.getLong()
    return UUID(firstLong, secondLong)
}

fun UUID.toBytes(): ByteArray {
    val bb = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(mostSignificantBits)
    bb.putLong(leastSignificantBits)
    return bb.array()
}
