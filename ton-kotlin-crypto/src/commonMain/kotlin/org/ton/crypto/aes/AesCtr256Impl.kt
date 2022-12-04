package org.ton.crypto.aes

import kotlin.experimental.xor

internal class AesCtr256Impl(
    key: ByteArray,
    iv: ByteArray
) {
    private val aes = Aes256Impl(key)
    private val iv = iv.copyOf(16)
    private var counter = Aes256Impl.BLOCK_SIZE
    private val buffer = ByteArray(Aes256Impl.BLOCK_SIZE)

    fun encrypt(input: ByteArray, output: ByteArray): ByteArray {
        for (i in input.indices) {
            if (counter == Aes256Impl.BLOCK_SIZE) {
                iv.copyInto(buffer)
                aes.encryptBlock(buffer, 0, buffer, 0)

                for (j in Aes256Impl.BLOCK_SIZE - 1 downTo 0) {
                    if (iv[j] == 0xFF.toByte()) {
                        iv[j] = 0
                    } else {
                        iv[j]++
                        break
                    }
                }
                counter = 0
            }
            output[i] = input[i] xor buffer[counter]
            counter++
        }
        return output
    }
}
