package org.ton.api.tcp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.pub.PublicKey
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@SerialName("tcp.authentificationComplete")
@Serializable
data class TcpAuthentificationComplete(
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

    companion object : TlCodec<TcpAuthentificationComplete> by TcpAuthentificationCompleteTlConstructor
}

private object TcpAuthentificationCompleteTlConstructor : TlConstructor<TcpAuthentificationComplete>(
    schema = "tcp.authentificationComplete key:PublicKey signature:bytes = tcp.Message"
) {
    override fun decode(input: Input): TcpAuthentificationComplete {
        val key = input.readTl(PublicKey)
        val signature = input.readBytesTl()
        return TcpAuthentificationComplete(key, signature)
    }

    override fun encode(output: Output, value: TcpAuthentificationComplete) {
        output.writeTl(PublicKey, value.key)
        output.writeBytesTl(value.signature)
    }
}
