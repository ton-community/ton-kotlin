package org.ton.api.adnl

import io.ktor.utils.io.core.*
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

data class AdnlProxyControlPacketRegister(
    val ip: Int,
    val port: Int
) : AdnlProxyControlPacket {
    companion object {
        fun tlConstructor(): TlConstructor<AdnlProxyControlPacketRegister> = AdnlProxyControlPacketRegisterTlConstructor
    }
}

private object AdnlProxyControlPacketRegisterTlConstructor : TlConstructor<AdnlProxyControlPacketRegister>(
    type = AdnlProxyControlPacketRegister::class,
    schema = "adnl.proxyControlPacketRegister ip:int port:int = adnl.ProxyControlPacket"
) {
    override fun encode(output: Output, value: AdnlProxyControlPacketRegister) {
        output.writeIntTl(value.ip)
        output.writeIntTl(value.port)
    }

    override fun decode(input: Input): AdnlProxyControlPacketRegister {
        val ip = input.readIntTl()
        val port = input.readIntTl()
        return AdnlProxyControlPacketRegister(ip, port)
    }
}
