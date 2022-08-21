package org.ton.api.adnl

import org.ton.tl.TlCombinator

sealed interface AdnlProxyControlPacket {
    companion object : TlCombinator<AdnlProxyControlPacket>(
        AdnlProxyControlPacketPing.tlConstructor(),
        AdnlProxyControlPacketPong.tlConstructor(),
        AdnlProxyControlPacketRegister.tlConstructor(),
    ) {
        fun tlCombinator(): TlCombinator<AdnlProxyControlPacket> = this
    }
}
