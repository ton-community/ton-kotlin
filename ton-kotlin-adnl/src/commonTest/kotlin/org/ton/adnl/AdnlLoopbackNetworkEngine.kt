package org.ton.adnl

import io.ktor.utils.io.core.*
import kotlinx.coroutines.channels.Channel
import org.ton.adnl.engine.AdnlNetworkEngine
import org.ton.api.adnl.AdnlAddressList
import org.ton.api.adnl.AdnlAddressUdp

class AdnlLoopbackNetworkEngine : AdnlNetworkEngine {
    private val channel = Channel<Pair<AdnlAddressUdp, ByteReadPacket>>()

    override suspend fun sendDatagram(adnlAddress: AdnlAddressUdp, payload: ByteReadPacket) {
        channel.send(adnlAddress to payload)
    }

    override suspend fun receiveDatagram(): Pair<AdnlAddressUdp, ByteReadPacket> {
        return channel.receive()
    }

    companion object {
        val DUMMY_ADDRESS_LIST = AdnlAddressList(
            addrs = listOf(
                AdnlAddressUdp(1, 1)
            )
        )
    }
}
