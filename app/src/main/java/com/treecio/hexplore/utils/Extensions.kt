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

private val hexArray = "0123456789ABCDEF".toCharArray()
fun ByteArray.toHexString(): String {
    val hexChars = CharArray(size * 2)
    for (j in 0 until size) {
        val v = this[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v ushr 4]
        hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
}

fun String.fromHexStringToByteArray(): ByteArray {
    val len = length
    val data = ByteArray(len / 2)
    var i = 0
    while (i < len) {
        data[i / 2] = ((Character.digit(this[i], 16) shl 4) + Character.digit(this[i + 1], 16)).toByte()
        i += 2
    }
    return data
}

