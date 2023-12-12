package org.ton.crypto.digest.sha2

import io.ktor.utils.io.bits.*

/**
 * FIPS 180-2 implementation of SHA-512.
 *
 * ```
 *         block  word  digest
 * SHA-1   512    32    160
 * SHA-256 512    32    256
 * SHA-384 1024   64    384
 * SHA-512 1024   64    512
 */
@Suppress("DEPRECATION")
@Deprecated("Use kotlinx-crypto instead")
public class SHA512Digest : LongDigest() {
    init {
        reset()
    }

    override val algorithmName: String get() = ALGORITHM_NAME
    override val digestSize: Int get() = SIZE_BYTES

    override fun build(output: ByteArray, offset: Int): ByteArray {
        finish()
        output.useMemory(0, output.size) {
            it.storeLongAt(offset, h1)
            it.storeLongAt(offset + 8, h2)
            it.storeLongAt(offset + 16, h3)
            it.storeLongAt(offset + 24, h4)
            it.storeLongAt(offset + 32, h5)
            it.storeLongAt(offset + 40, h6)
            it.storeLongAt(offset + 48, h7)
            it.storeLongAt(offset + 56, h8)
        }
        reset()
        return output
    }

    override fun reset() {
        super.reset()

        /*
        SHA-512 initial hash value
        The first 64 bits of the fractional parts of the square roots
        of the first eight prime numbers
        */
        h1 = 0x6a09e667f3bcc908L
        h2 = 0xbb67ae8584caa73buL.toLong()
        h3 = 0x3c6ef372fe94f82bL
        h4 = 0xa54ff53a5f1d36f1uL.toLong()
        h5 = 0x510e527fade682d1L
        h6 = 0x9b05688c2b3e6c1fuL.toLong()
        h7 = 0x1f83d9abfb41bd6bL
        h8 = 0x5be0cd19137e2179L
    }

    public companion object {
        public const val ALGORITHM_NAME: String = "SHA-512"
        public const val SIZE_BYTES: Int = 64
        public const val SIZE_BITS: Int = SIZE_BYTES * Byte.SIZE_BITS
    }
}
