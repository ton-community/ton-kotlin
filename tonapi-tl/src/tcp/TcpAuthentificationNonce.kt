package org.ton.api.tcp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.TlReader
import org.ton.tl.TlWriter

@SerialName("tcp.authentificationNonce")
@Serializable
public data class TcpAuthentificationNonce(
    val nonce: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TcpAuthentificate) return false
        if (!nonce.contentEquals(other.nonce)) return false
        return true
    }

    override fun hashCode(): Int = nonce.contentHashCode()

    public companion object : TlCodec<TcpAuthentificationNonce> by TcpAuthentificationNonceTlConstructor
}

private object TcpAuthentificationNonceTlConstructor : TlConstructor<TcpAuthentificationNonce>(
    schema = "tcp.authentificationNonce nonce:bytes = tcp.Message"
) {
    override fun decode(reader: TlReader): TcpAuthentificationNonce {
        val nonce = reader.readBytes()
        return TcpAuthentificationNonce(nonce)
    }

    override fun encode(writer: TlWriter, value: TcpAuthentificationNonce) {
        writer.writeBytes(value.nonce)
    }
}
