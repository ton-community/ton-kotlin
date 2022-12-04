package org.ton.crypto.aes

import io.ktor.utils.io.core.*

expect class AesCtr(key: ByteArray, iv: ByteArray) {
    fun update(input: ByteArray, output: ByteArray = ByteArray(input.size)): ByteArray
    fun doFinal(output: ByteArray): ByteArray
    fun doFinal(): ByteArray
}
