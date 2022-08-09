package org.ton.adnl.client.socket

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.coroutines.coroutineScope
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.coroutines.CoroutineContext

suspend fun ByteWriteChannel.cipher(coroutineContext: CoroutineContext, cipher: Cipher) = coroutineScope {
    reader(coroutineContext, autoFlush = true) {
        val buffer = ChunkBuffer.Pool.borrow()
        try {
            while (true) {
                buffer.reset()
                val size = channel.readAvailable(buffer)
                val bytes = if (size == -1) {
                    cipher.doFinal()
                    break
                } else {
                    val unencrypted = buffer.readBytes()
                    cipher.update(unencrypted)
                }
                writeFully(bytes)
                flush()
            }
        } catch (t: Throwable) {
            close(t)
            throw t
        } finally {
            buffer.release(ChunkBuffer.Pool)
        }
    }
}.channel

suspend fun ByteReadChannel.cipher(coroutineContext: CoroutineContext, cipher: Cipher) = coroutineScope {
    writer(coroutineContext, autoFlush = true) {
        val buffer = ChunkBuffer.Pool.borrow()
        try {
            while (true) {
                buffer.reset()
                val size = readAvailable(buffer)
                val bytes = if (size == -1) {
                    cipher.doFinal()
                    break
                } else {
                    cipher.update(buffer.readBytes())
                }
                channel.writeFully(bytes)
            }
        } catch (t: Throwable) {
            channel.close(t)
            throw t
        } finally {
            buffer.release(ChunkBuffer.Pool)
        }
    }
}.channel

fun cipher(key: ByteArray, iv: ByteArray) = Cipher.getInstance("AES/CTR/NoPadding")!!.also {
    it.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), IvParameterSpec(iv))
}

