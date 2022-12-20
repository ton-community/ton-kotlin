@file:JvmName("Sha256JvmKt")

package org.ton.crypto

import java.security.MessageDigest

public actual class Sha256 {
    private val digest = MessageDigest.getInstance("SHA-256")

    public actual fun update(bytes: ByteArray): Sha256 = apply {
        digest.update(bytes)
    }

    public actual fun digest(output: ByteArray): ByteArray {
        digest.digest().copyInto(output)
        return output
    }
}
