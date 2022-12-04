package org.ton.crypto.aes

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

actual class AesCtr actual constructor(
    key: ByteArray,
    iv: ByteArray,
) {
    private val cipher = Cipher.getInstance("AES/CTR/NoPadding").also {
        it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
    }

    actual fun doFinal() = cipher.doFinal()

    actual fun update(input: ByteArray, output: ByteArray): ByteArray {
        cipher.update(input, 0, input.size, output, 0)
        return output
    }

    actual fun doFinal(output: ByteArray): ByteArray {
        cipher.doFinal(output, 0)
        return output
    }
}
