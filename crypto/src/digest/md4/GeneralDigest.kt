package org.ton.crypto.digest.md4

import org.ton.crypto.digest.Digest

/**
 * base implementation of MD4 family style digest as outlined in "Handbook of Applied Cryptography",
 * pages 344 - 347.
 */
@Deprecated("Use kotlinx-crypto instead")
public abstract class GeneralDigest : Digest {
    private val xBuf = ByteArray(4)
    private var xBuffOff = 0
    private var byteCount = 0

    public fun update(input: Byte) {
        xBuf[xBuffOff++] = input
        if (xBuffOff == xBuf.size) {
            processWord(xBuf, 0)
            xBuffOff = 0
        }
        byteCount++
    }

    public override fun update(input: ByteArray, offset: Int, length: Int) {
        // fill the current word
        var i = 0
        if (xBuffOff != 0) {
            while (i < length) {
                xBuf[xBuffOff++] = input[offset + i++]
                if (xBuffOff == 4) {
                    processWord(xBuf, 0)
                    xBuffOff = 0
                    break
                }
            }
        }

        // process whole words.
        val limit = ((length - i) and 3.inv()) + i
        while (i < limit) {
            processWord(input, offset + i)
            i += 4
        }

        // load in the remainder.
        while (i < length) {
            xBuf[xBuffOff++] = input[offset + i++]
        }

        byteCount += length
    }

    public fun finish() {
        val bitLength = byteCount.toLong() shl 3

        // add the pad bytes.
        update(128.toByte())

        while (xBuffOff != 0) {
            update(0.toByte())
        }
        processLength(bitLength)
        processBlock()
    }

    override fun reset() {
        byteCount = 0
        xBuffOff = 0
        xBuf.fill(0)
    }

    protected abstract fun processWord(input: ByteArray, offset: Int)
    protected abstract fun processLength(bitLength: Long)
    protected abstract fun processBlock()
}
