package org.ton.crypto

import io.ktor.utils.io.core.*
import java.nio.ByteBuffer
import javax.crypto.Cipher

internal fun ByteReadPacket.cipherLoop(cipher: Cipher, header: BytePacketBuilder.() -> Unit = {}): ByteReadPacket {
    val srcBuffer = DefaultByteBufferPool.borrow()
    var dstBuffer = CryptoBufferPool.borrow()
    var dstBufferFromPool = true

    try {
        return buildPacket {
            srcBuffer.clear()
            header()

            while (true) {
                val rc = if (srcBuffer.hasRemaining()) readAvailable(srcBuffer) else 0
                srcBuffer.flip()

                if (!srcBuffer.hasRemaining() && (rc == -1 || this@cipherLoop.isEmpty)) break

                dstBuffer.clear()

                if (cipher.getOutputSize(srcBuffer.remaining()) > dstBuffer.remaining()) {
                    if (dstBufferFromPool) {
                        CryptoBufferPool.recycle(dstBuffer)
                    }
                    dstBuffer = ByteBuffer.allocate(cipher.getOutputSize(srcBuffer.remaining()))
                    dstBufferFromPool = false
                }

                cipher.update(srcBuffer, dstBuffer)
                dstBuffer.flip()
                writeFully(dstBuffer)
                srcBuffer.compact()
            }

            assert(!srcBuffer.hasRemaining()) { "Cipher loop completed too early: there are unprocessed bytes" }
            assert(!dstBuffer.hasRemaining()) { "Not all bytes were appended to the packet" }

            val requiredBufferSize = cipher.getOutputSize(0)
            if (requiredBufferSize == 0) return@buildPacket
            if (requiredBufferSize > dstBuffer.capacity()) {
                writeFully(cipher.doFinal())
                return@buildPacket
            }

            dstBuffer.clear()
            cipher.doFinal(EmptyByteBuffer, dstBuffer)
            dstBuffer.flip()

            if (!dstBuffer.hasRemaining()) { // workaround JDK bug
                writeFully(cipher.doFinal())
                return@buildPacket
            }

            writeFully(dstBuffer)
        }
    } finally {
        DefaultByteBufferPool.recycle(srcBuffer)
        if (dstBufferFromPool) {
            CryptoBufferPool.recycle(dstBuffer)
        }
    }
}
