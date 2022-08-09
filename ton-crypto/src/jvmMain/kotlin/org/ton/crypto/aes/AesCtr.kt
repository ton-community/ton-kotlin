@file:OptIn(ExperimentalContracts::class)

package org.ton.crypto.aes

import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.ton.crypto.encodeHex
import java.nio.ByteBuffer
import java.security.Security
import java.util.concurrent.atomic.AtomicLong
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.contracts.ExperimentalContracts


actual class AesCtr actual constructor(
    private val key: ByteArray,
    private val iv: ByteArray,
) {
    private val bytesEncrypted = AtomicLong(0L)
    private val _cipherEncrypt = Cipher.getInstance("AES/CTR/NoPadding", "BC").also {
        it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
    }

    fun getOutputSize(inputLen: Int) = _cipherEncrypt.getOutputSize(inputLen)

    fun update(input: ByteBuffer, output: ByteBuffer) = _cipherEncrypt.update(input, output)

    fun doFinal(input: ByteBuffer, output: ByteBuffer) = _cipherEncrypt.doFinal()

    actual fun encrypt(byteArray: ByteArray): ByteArray = runBlocking {
        if (byteArray.isEmpty()) throw IllegalStateException("empty buffer")
        withContext(context) {
            val r = _cipherEncrypt.update(byteArray)
            val r2 = _cipherEncrypt.doFinal()
            bytesEncrypted.addAndGet(byteArray.size.toLong())
            // 532bfe10e871d176a29ad857cfd46e7a28d60a16ab6ef3f2e3912506d64b0d241f4b7d49ea01bdd3ae1859b726fb13d9086db6576eef90c3adc0a4637bfac4d07170328459d5098c98af44dcee241461f6cdc274792af1dec4916459d71b0c987a98ca613656251a3fabb0d95bde350276ec287348090796c803c0e336f2ce676ffaea5034d58ddbe4140594c5bb5bb5b2365245ee40508960b4fa4c49d02d22
            r + r2
        }
    }

    override fun toString(): String =
        "AES(${_cipherEncrypt.hashCode().toString(16)} $bytesEncrypted, key=${key.encodeHex()}, iv=${iv.encodeHex()})"

    companion object {
        private val context = newSingleThreadContext("AES")

        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }
}
