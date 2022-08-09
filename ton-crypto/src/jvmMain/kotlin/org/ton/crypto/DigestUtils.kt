package org.ton.crypto

import io.ktor.utils.io.core.*
import java.security.MessageDigest

internal fun Digest(): Digest = Digest(BytePacketBuilder())

@JvmInline
internal value class Digest(val state: BytePacketBuilder) : Closeable {

    fun update(packet: ByteReadPacket) = synchronized(state) {
        if (packet.isEmpty) return
        state.writePacket(packet.copy())
    }

    fun doHash(hashName: String): ByteArray = synchronized(state) {
        state.preview { handshakes: ByteReadPacket ->
            val digest = MessageDigest.getInstance(hashName)!!

            val buffer = DefaultByteBufferPool.borrow()
            try {
                while (!handshakes.isEmpty) {
                    val rc = handshakes.readAvailable(buffer)
                    if (rc == -1) break
                    buffer.flip()
                    digest.update(buffer)
                    buffer.clear()
                }

                return@preview digest.digest()
            } finally {
                DefaultByteBufferPool.recycle(buffer)
            }
        }
    }

    override fun close() {
        state.release()
    }
}
