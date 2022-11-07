package org.ton.proxy.adnl

import org.ton.api.adnl.AdnlPacketContents

interface AdnlPacketReceiver : AdnlMessageReceiver {
    fun receivePacket(packet: AdnlPacketContents) {
        packet.messages().forEach {
            receiveMessage(it)
        }
    }
}
