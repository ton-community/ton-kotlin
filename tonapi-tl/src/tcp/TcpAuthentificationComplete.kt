package org.ton.api.tcp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKey
import org.ton.tl.*

@SerialName("tcp.authentificationComplete")
@Serializable
public data class TcpAuthentificationComplete(
    val key: PublicKey,
    val signature: ByteArray
) : TcpMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TcpAuthentificationComplete) return false
        if (key != other.key) return false
        if (!signature.contentEquals(other.signature)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + signature.contentHashCode()
        return result
    }

    public companion object : TlCodec<TcpAuthentificationComplete> by TcpAuthentificationCompleteTlConstructor
}

private object TcpAuthentificationCompleteTlConstructor : TlConstructor<TcpAuthentificationComplete>(
    schema = "tcp.authentificationComplete key:PublicKey signature:bytes = tcp.Message"
) {
    override fun decode(reader: TlReader): TcpAuthentificationComplete {
        val key = reader.read(PublicKey)
        val signature = reader.readBytes()
        return TcpAuthentificationComplete(key, signature)
    }

    override fun encode(writer: TlWriter, value: TcpAuthentificationComplete) {
        writer.write(PublicKey, value.key)
        writer.writeBytes(value.signature)
    }
}
