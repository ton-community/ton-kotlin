package org.ton.crypto

import java.util.zip.CRC32

actual fun crc32(byteArray: ByteArray, offset: Int, length: Int): Int {
    return CRC32().apply {
        update(byteArray, offset, length)
    }.value.toInt()
}