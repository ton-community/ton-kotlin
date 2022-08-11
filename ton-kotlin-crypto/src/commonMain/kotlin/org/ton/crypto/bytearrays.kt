package org.ton.crypto

import io.ktor.util.*

fun ByteArray(string: String): ByteArray {
    if (string.isEmpty()) return ByteArray(0)
    return try {
        string.decodeBase64Bytes()
    } catch (e: Exception) {
        string.decodeHex()
    }
}
