package org.ton.api.adnl

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeInt256Tl

data class AdnlProxyControlPacketPong(
    val id: ByteArray
) : AdnlProxyControlPacket {
    fun isValid(ping: AdnlProxyControlPacketPing): Boolean =
        id.contentEquals(ping.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyControlPacketPong

        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }

    companion object {
        fun tlConstructor(): TlConstructor<AdnlProxyControlPacketPong> = AdnlProxyControlPacketPongTlConstructor
    }
}

private object AdnlProxyControlPacketPongTlConstructor : TlConstructor<AdnlProxyControlPacketPong>(
    type = AdnlProxyControlPacketPong::class,
    schema = "adnl.proxyControlPacketPong id:int256 = adnl.ProxyControlPacket"
) {
    override fun encode(output: Output, value: AdnlProxyControlPacketPong) {
        output.writeInt256Tl(value.id)
    }

    override fun decode(input: Input): AdnlProxyControlPacketPong {
        val id = input.readInt256Tl()
        return AdnlProxyControlPacketPong(id)
    }
}
