package org.ton.bitstring

internal actual fun ByteArray.setInt(index: Int, value: Int) {
    this[index] = (value ushr 24).toByte()
    this[index + 1] = (value ushr 16).toByte()
    this[index + 2] = (value ushr 8).toByte()
    this[index + 3] = value.toByte()
}

internal actual fun ByteArray.getInt(index: Int): Int {
    return (this[index].toInt() and 0xFF) shl 24 or
            (this[index + 1].toInt() and 0xFF) shl 16 or
            (this[index + 2].toInt() and 0xFF) shl 8 or
            (this[index + 3].toInt() and 0xFF)
}

internal actual fun ByteArray.setLong(index: Int, value: Long) {
    this[index] = (value shr 56).toByte()
    this[index + 1] = (value shr 48).toByte()
    this[index + 2] = (value shr 40).toByte()
    this[index + 3] = (value shr 32).toByte()
    this[index + 4] = (value shr 24).toByte()
    this[index + 5] = (value shr 16).toByte()
    this[index + 6] = (value shr 8).toByte()
    this[index + 7] = value.toByte()
}