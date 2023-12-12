package org.ton.crypto.mac.hmac

import org.ton.crypto.digest.Digest
import org.ton.crypto.mac.Mac

/**
 * HMAC implementation based on RFC2104
 *
 * H(K XOR opad, H(K XOR ipad, text))
 */
@Suppress("DEPRECATION")
public class HMac private constructor(
    private val digest: Digest,
    private val blockLength: Int
) : Mac {
    public constructor(digest: Digest) : this(digest, byteLength(digest))
    public constructor(digest: Digest, key: ByteArray) : this(digest) {
        init(key)
    }

    public override val algorithmName: String get() = "HMAC/${digest.algorithmName}"
    public override val macSize: Int get() = digestSize

    private val digestSize = digest.digestSize
    private val inputPad = ByteArray(blockLength)
    private val outputBuf = ByteArray(blockLength + digestSize)

    public override fun init(key: ByteArray): HMac = apply {
        digest.reset()
        var keyLength = key.size

        if (keyLength > blockLength) {
            digest.update(key)
            digest.build(inputPad)
            keyLength = digestSize
        } else {
            key.copyInto(inputPad)
        }

        inputPad.fill(0, keyLength, inputPad.size)
        inputPad.copyInto(outputBuf)

        xorPad(inputPad, 0, blockLength, IPAD)
        xorPad(outputBuf, 0, blockLength, OPAD)

        digest.update(inputPad)
    }

    public override fun update(input: ByteArray, offset: Int, length: Int) {
        digest.update(input, offset, length)
    }

    public override fun build(output: ByteArray, offset: Int): ByteArray = output.apply {
        digest.build(outputBuf, blockLength)
        digest.update(outputBuf)
        digest.build(output, offset)
        outputBuf.fill(0, blockLength, outputBuf.size)
        digest.update(inputPad)
    }

    public override fun reset() {
        digest.reset()
        digest.update(inputPad)
    }
}

private const val IPAD = 0x36.toByte()
private const val OPAD = 0x5C.toByte()

private fun byteLength(digest: Digest) = when (digest.algorithmName) {
    "SHA-256" -> 64
    "SHA-512" -> 128
    else -> throw IllegalArgumentException("Unsupported digest algorithm: ${digest.algorithmName}")
}

private fun xorPad(pad: ByteArray, offset: Int, length: Int, value: Byte) {
    for (i in offset until offset + length) {
        pad[i] = (pad[i].toInt() xor value.toInt()).toByte()
    }
}
