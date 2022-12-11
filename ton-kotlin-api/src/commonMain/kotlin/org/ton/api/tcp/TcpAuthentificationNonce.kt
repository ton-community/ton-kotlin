package org.ton.api.tcp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@SerialName("tcp.authentificationNonce")
@Serializable
data class TcpAuthentificationNonce(
    val nonce: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TcpAuthentificate) return false
        if (!nonce.contentEquals(other.nonce)) return false
        return true
    }

    override fun hashCode(): Int = nonce.contentHashCode()

    companion object : TlCodec<TcpAuthentificationNonce> by TcpAuthentificationNonceTlConstructor
}

private object TcpAuthentificationNonceTlConstructor : TlConstructor<TcpAuthentificationNonce>(
    schema = "tcp.authentificationNonce nonce:bytes = tcp.Message"
) {
    override fun decode(input: Input): TcpAuthentificationNonce {
        val nonce = input.readBytesTl()
        return TcpAuthentificationNonce(nonce)
    }

    override fun encode(output: Output, value: TcpAuthentificationNonce) {
        output.writeBytesTl(value.nonce)
    }
}
