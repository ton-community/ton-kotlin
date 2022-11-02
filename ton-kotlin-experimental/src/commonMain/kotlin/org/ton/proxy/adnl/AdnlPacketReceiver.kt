package org.ton.proxy.adnl

import org.ton.adnl.client.AdnlPacket
import org.ton.api.adnl.AdnlPacketContents

interface AdnlPacketReceiver : AdnlMessageReceiver{
    fun receivePacket(packet: AdnlPacketContents) {
        packet.messages().forEach {
            receiveMessage(it)
        }
    }
}
