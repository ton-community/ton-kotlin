package org.ton.crypto

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

actual fun pbkdf2Sha512(key: CharArray, salt: ByteArray, iterations: Int): ByteArray {
    val spec = PBEKeySpec(key, salt, iterations, 512)

    return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec).encoded
}
