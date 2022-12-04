package org.ton.crypto.sha256

import java.security.MessageDigest

actual class Sha256 {
    private val digest = MessageDigest.getInstance("SHA-256")

    actual fun update(bytes: ByteArray): Sha256 = apply {
        digest.update(bytes)
    }

    actual fun digest(output: ByteArray): ByteArray {
        digest.digest().copyInto(output)
        return output
    }
}
