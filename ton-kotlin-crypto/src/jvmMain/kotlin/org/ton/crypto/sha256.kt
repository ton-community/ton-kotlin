package org.ton.crypto

import io.ktor.utils.io.core.*
import java.security.MessageDigest

actual fun sha256(vararg bytes: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    bytes.forEach {
        digest.update(it)
    }
    return digest.digest()
}

actual fun sha256(builder: BytePacketBuilder.() -> Unit) = Digest(BytePacketBuilder()).doHash("SHA-256")

