package org.ton.crypto.digest

import org.ton.crypto.digest.sha2.SHA256Digest
import org.ton.crypto.digest.sha2.SHA512Digest

public actual fun Digest(algorithm: String): Digest = when (algorithm) {
    SHA256Digest.ALGORITHM_NAME -> SHA256Digest()
    SHA512Digest.ALGORITHM_NAME -> SHA512Digest()
    else -> throw IllegalArgumentException("Unsupported digest algorithm: $algorithm")
}

public actual fun sha256(bytes: ByteArray): ByteArray = Digest("SHA-256").apply {
    update(bytes)
}.build()

public actual fun sha512(bytes: ByteArray): ByteArray = Digest("SHA-512").apply {
    update(bytes)
}.build()
