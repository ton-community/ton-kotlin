package org.ton.crypto

import java.nio.ByteBuffer
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

actual fun pbkdf2Sha512(key: ByteArray, salt: ByteArray, iterations: Int): ByteArray {
    val spec = PBEKeySpec(Charsets.US_ASCII.decode(ByteBuffer.wrap(key)).array(), salt, iterations, 512)

    return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec).encoded
}
