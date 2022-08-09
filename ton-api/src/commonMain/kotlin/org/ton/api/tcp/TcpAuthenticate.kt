package org.ton.api.tcp

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl

data class TcpAuthenticate(
    val nonce: ByteArray
) : TcpMessage {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TcpAuthenticate

        if (!nonce.contentEquals(other.nonce)) return false

        return true
    }

    override fun hashCode(): Int {
        return nonce.contentHashCode()
    }

    companion object {
        @JvmStatic
        fun tlConstructor(): TlConstructor<TcpAuthenticate> = TcpAuthenticateTlConstructor
    }
}

private object TcpAuthenticateTlConstructor : TlConstructor<TcpAuthenticate>(
    type = TcpAuthenticate::class,
    schema = "tcp.authentificate nonce:bytes = tcp.Message"
) {
    override fun decode(input: Input): TcpAuthenticate {
        val nonce = input.readBytesTl()
        return TcpAuthenticate(nonce)
    }

    override fun encode(output: Output, value: TcpAuthenticate) {
        output.writeBytesTl(value.nonce)
    }
}
