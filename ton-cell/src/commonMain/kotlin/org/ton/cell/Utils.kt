package org.ton.cell

import io.ktor.utils.io.core.*

internal fun Input.readInt(bytes: Int): Int {
    var result = 0
    var b = bytes
    while (b > 0) {
        result = (result shl 8) + readByte()
        b--
    }
    return result
}

internal fun Output.writeInt(value: Int, bytes: Int) {
    var v = value
    var b = bytes
    while (b > 0) {
        writeByte((v and 0xff).toByte())
        v = v shr 8
        b--
    }
}
