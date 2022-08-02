@file:OptIn(ExperimentalContracts::class)

package org.ton.crypto.aes

import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.contracts.ExperimentalContracts

actual class AesCtr actual constructor(
    key: ByteArray,
    iv: ByteArray,
) {
    private val _cipherEncrypt = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
    }

    actual fun encrypt(byteArray: ByteArray): ByteArray = runBlocking {
        if (byteArray.isEmpty()) throw IllegalStateException("empty buffer")
        withContext(context) {
            val r = _cipherEncrypt.update(byteArray)
            r
        }
    }

    companion object {
        private val context = newSingleThreadContext("AES")
    }
}
