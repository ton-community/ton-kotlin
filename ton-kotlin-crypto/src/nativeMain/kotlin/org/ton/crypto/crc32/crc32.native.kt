package org.ton.crypto.crc32

actual fun crc32(bytes: ByteArray, offset: Int, size: Int): Int =
    crc32update(IEEE_TABLE, bytes, offset, size)

actual fun crc32c(bytes: ByteArray, offset: Int, size: Int): Int =
    crc32update(CASTAGNOLI_TABLE, bytes, offset, size)
