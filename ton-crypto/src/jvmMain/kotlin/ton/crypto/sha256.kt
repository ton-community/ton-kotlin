package ton.crypto

import java.security.MessageDigest

actual suspend fun sha256(vararg bytes: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    bytes.forEach {
        digest.update(it)
    }
    return digest.digest()
}