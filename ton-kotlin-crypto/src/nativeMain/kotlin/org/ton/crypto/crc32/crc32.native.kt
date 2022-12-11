package org.ton.crypto.crc32

actual fun crc32(bytes: ByteArray, fromIndex: Int, toIndex: Int): Int =
    crc32slicingUpdate(0, IEEE_TABLE, bytes, fromIndex, toIndex)

actual fun crc32c(bytes: ByteArray, fromIndex: Int, toIndex: Int): Int =
    crc32slicingUpdate(0, CASTAGNOLI_TABLE, bytes, fromIndex, toIndex)
