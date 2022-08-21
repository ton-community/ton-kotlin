package org.ton.api.adnl

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readInt256Tl
import org.ton.tl.constructors.writeInt256Tl

data class AdnlProxyControlPacketPing(
    val id: ByteArray
) : AdnlProxyControlPacket {
    fun isValid(pong: AdnlProxyControlPacketPong): Boolean =
        id.contentEquals(pong.id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdnlProxyControlPacketPing

        if (!id.contentEquals(other.id)) return false

        return true
    }

    override fun hashCode(): Int {
        return id.contentHashCode()
    }

    companion object {
        fun tlConstructor(): TlConstructor<AdnlProxyControlPacketPing> = AdnlProxyControlPacketPingTlConstructor
    }
}

private object AdnlProxyControlPacketPingTlConstructor : TlConstructor<AdnlProxyControlPacketPing>(
    type = AdnlProxyControlPacketPing::class,
    schema = "adnl.proxyControlPacketPing id:int256 = adnl.ProxyControlPacket"
) {
    override fun encode(output: Output, value: AdnlProxyControlPacketPing) {
        output.writeInt256Tl(value.id)
    }

    override fun decode(input: Input): AdnlProxyControlPacketPing {
        val id = input.readInt256Tl()
        return AdnlProxyControlPacketPing(id)
    }
}
