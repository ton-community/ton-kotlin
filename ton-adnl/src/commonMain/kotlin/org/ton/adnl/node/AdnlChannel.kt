package org.ton.adnl.node

import org.ton.api.pub.PublicKeyAes

class AdnlSubChannelSide(
    val id: ByteArray = ByteArray(32),
    val key: PublicKeyAes,
    val priority: Boolean = false
)

class AdnlChannelSide(
    val ordinary: AdnlSubChannelSide,
    val priority: AdnlSubChannelSide
)

class AdnlChannel(
    val recv: AdnlChannelSide,
    val send: AdnlChannelSide
)
