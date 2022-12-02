package org.ton.crypto.crc32

import java.util.zip.CRC32

// using CRC32 from java.util.zip due native implementation
actual fun crc32(bytes: ByteArray, offset: Int, size: Int): Int = CRC32().apply {
    update(bytes, offset, size)
}.value.toInt()

// using basic implementation due CRC32C from java.util.zip is not supported on JVM 1.8
actual fun crc32c(bytes: ByteArray, offset: Int, size: Int): Int =
    crc32update(CASTAGNOLI_TABLE, bytes, offset, size)
