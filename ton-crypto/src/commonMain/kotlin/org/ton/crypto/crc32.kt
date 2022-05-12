package org.ton.crypto

fun crc32(string: String): Int = crc32(string.encodeToByteArray())

fun crc32(vararg byteArrays: ByteArray): Int {
    var crc = 0
    for (byteArray in byteArrays) {
        crc = crc32(byteArray)
    }
    return crc
}

expect fun crc32(byteArray: ByteArray, offset: Int = 0, length: Int = byteArray.size): Int