package org.ton.crypto

@Deprecated(
    message = "Use kotlinx-crypto instead",
    replaceWith = ReplaceWith("io.github.andreypfau.kotlinx.crypto.crc32.crc32(bytes.copyOfRange(fromIndex, toIndex))")
)
public fun crc32(bytes: ByteArray, fromIndex: Int = 0, toIndex: Int = bytes.size - fromIndex): Int =
    io.github.andreypfau.kotlinx.crypto.crc32.crc32(bytes.copyOfRange(fromIndex, toIndex))

@Deprecated(
    message = "Use kotlinx-crypto instead",
    replaceWith = ReplaceWith("io.github.andreypfau.kotlinx.crypto.crc32.crc32c(bytes.copyOfRange(fromIndex, toIndex))")
)
public fun crc32c(bytes: ByteArray, fromIndex: Int = 0, toIndex: Int = bytes.size - fromIndex): Int =
    io.github.andreypfau.kotlinx.crypto.crc32.crc32c(bytes.copyOfRange(fromIndex, toIndex))
