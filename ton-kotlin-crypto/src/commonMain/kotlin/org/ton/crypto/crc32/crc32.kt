package org.ton.crypto.crc32

// IEEE is by far and away the most common CRC-32 polynomial.
// Used by ethernet (IEEE 802.3), v.42, fddi, gzip, zip, png, ...
internal val IEEE_TABLE by lazy {
    crc32table(0xedb88320.toInt())
}

// Castagnoli's polynomial, used in iSCSI.
// Has better error detection characteristics than IEEE.
// https://dx.doi.org/10.1109/26.231911
internal val CASTAGNOLI_TABLE by lazy {
    crc32table(0x82f63b78.toInt())
}

expect fun crc32(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size): Int
expect fun crc32c(bytes: ByteArray, offset: Int = 0, size: Int = bytes.size): Int

private fun crc32table(poly: Int) = IntArray(256) {
    var crc = it
    for (j in 0 until 8) {
        crc = if (crc and 1 != 0) {
            (crc ushr 1) xor poly
        } else {
            crc ushr 1
        }
    }
    crc
}

internal fun crc32update(table: IntArray, bytes: ByteArray, offset: Int, size: Int): Int {
    var crc = 0
    for (i in offset until offset + size) {
        crc = table[(crc xor bytes[i].toInt()) and 0xff] xor (crc ushr 8)
    }
    return crc
}
