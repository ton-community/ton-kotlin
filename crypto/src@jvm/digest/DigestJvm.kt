package org.ton.crypto.digest

import java.security.MessageDigest

public actual fun Digest(algorithm: String): Digest = DigestImpl(MessageDigest.getInstance(algorithm))

public actual fun sha256(bytes: ByteArray): ByteArray = MessageDigest.getInstance("SHA-256").digest(bytes)

public actual fun sha512(bytes: ByteArray): ByteArray = MessageDigest.getInstance("SHA-512").digest(bytes)

@JvmInline
private value class DigestImpl(
    val digest: MessageDigest
) : Digest {
    override val algorithmName: String
        get() = digest.algorithm
    override val digestSize: Int
        get() = digest.digestLength

    override fun update(input: ByteArray, offset: Int, length: Int) {
        digest.update(input, offset, length)
    }

    override fun build(output: ByteArray, offset: Int): ByteArray {
        digest.digest(output, offset, output.size - offset)
        return output
    }

    override fun reset() {
        digest.reset()
    }
}
