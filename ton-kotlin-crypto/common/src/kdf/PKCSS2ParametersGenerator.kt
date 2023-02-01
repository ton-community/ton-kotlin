package org.ton.crypto.kdf

import org.ton.crypto.digest.Digest
import org.ton.crypto.mac.hmac.HMac

public class PKCSS2ParametersGenerator(
    private val hMac: HMac,
    password: ByteArray,
    salt: ByteArray,
    iterationCount: Int
) : PBEParametersGenerator(password, salt, iterationCount) {
    public constructor(digest: Digest, password: ByteArray, salt: ByteArray, iterationCount: Int) : this(
        HMac(digest),
        password,
        salt,
        iterationCount
    )
    public constructor(hMac: HMac) : this(hMac, ByteArray(0), ByteArray(0), 0)
    public constructor(digest: Digest) : this(digest, ByteArray(0), ByteArray(0), 0)

    private val state = ByteArray(hMac.macSize)

    override fun generateDerivedParameters(keySize: Int): ByteArray =
        generateDerivedKey(keySize / 8)

    override fun generateDerivedMacParameters(keySize: Int): ByteArray =
        generateDerivedParameters(keySize)

    private fun generateDerivedKey(dkLen: Int): ByteArray {
        val hLen = hMac.macSize
        val l = (dkLen + hLen - 1) / hLen
        val iBuf = ByteArray(4)
        val dk = ByteArray(l * hLen)
        var outOff = 0

        hMac.init(password)
        for (i in 1..l) {
            iBuf[0] = (i ushr 24).toByte()
            iBuf[1] = (i ushr 16).toByte()
            iBuf[2] = (i ushr 8).toByte()
            iBuf[3] = i.toByte()
            f(salt, iterationCount, iBuf, dk, outOff)
            outOff += hLen
        }

        return dk
    }

    private fun f(
        s: ByteArray,
        c: Int,
        iBuf: ByteArray,
        output: ByteArray,
        outOff: Int
    ) {
        if (s.isNotEmpty()) {
            hMac.update(s)
        }
        hMac.update(iBuf)
        hMac.build(state)

        state.copyInto(output, outOff)

        for (count in 1 until c) {
            hMac.update(state)
            hMac.build(state)
            for (j in state.indices) {
                output[outOff + j] = (output[outOff + j].toInt() xor state[j].toInt()).toByte()
            }
        }
    }
}
