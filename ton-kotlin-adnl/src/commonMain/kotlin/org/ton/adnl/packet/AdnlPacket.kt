package org.ton.adnl.packet

import io.ktor.utils.io.core.*
import org.ton.adnl.node.AdnlChannel
import org.ton.api.adnl.AdnlPacketContents
import org.ton.api.pub.PublicKey
import org.ton.crypto.encodeHex

sealed interface AdnlPacket {
    fun build(): ByteReadPacket
}

class AdnlHandshakePacket(
    val payload: ByteArray,
    val publicKey: PublicKey
) : AdnlPacket {
    constructor(
        contents: AdnlPacketContents,
        publicKey: PublicKey
    ) : this(contents.toByteArray(), publicKey)

    override fun build() = buildPacket {
        writeFully(publicKey.toAdnlIdShort().id)
        writeFully(publicKey.encrypt(payload))
    }

    override fun toString(): String =
        "AdnlHandshakePacket(\n  payload=${payload.encodeHex()}\n  publicKey=$publicKey\n)"
}

class AdnlChannelPacket(
    val contents: AdnlPacketContents,
    val channel: AdnlChannel
) : AdnlPacket {
    override fun build() = buildPacket {
        channel.encrypt(contents.toByteArray())
    }
}
