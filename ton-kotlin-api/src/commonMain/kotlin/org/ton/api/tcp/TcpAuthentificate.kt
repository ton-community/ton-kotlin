package org.ton.api.tcp

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

@SerialName("tcp.authentificate")
@Serializable
data class TcpAuthentificate(
    val nonce: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TcpAuthentificate) return false
        if (!nonce.contentEquals(other.nonce)) return false
        return true
    }

    override fun hashCode(): Int = nonce.contentHashCode()

    companion object : TlCodec<TcpAuthentificate> by TcpAuthentificateTlConstructor
}

private object TcpAuthentificateTlConstructor : TlConstructor<TcpAuthentificate>(
    schema = "tcp.authentificate nonce:bytes = tcp.Authentificate"
) {
    override fun decode(input: Input): TcpAuthentificate {
        val nonce = input.readBytesTl()
        return TcpAuthentificate(nonce)
    }

    override fun encode(output: Output, value: TcpAuthentificate) {
        output.writeBytesTl(value.nonce)
    }
}
