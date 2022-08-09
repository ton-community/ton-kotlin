@file:OptIn(ExperimentalContracts::class)

package org.ton.crypto.aes

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

    actual fun update(input: ByteArray) = _cipherEncrypt.update(input)

    actual fun doFinal() = _cipherEncrypt.doFinal()
}
