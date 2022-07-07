package org.ton.lite.api.liteserver

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlCodec
import org.ton.tl.TlCombinator

@Suppress("PropertyName")
sealed interface LiteServerBlockLink {
    val to_key_block: Boolean
    val from: TonNodeBlockIdExt
    val to: TonNodeBlockIdExt

    companion object : TlCodec<LiteServerBlockLink> by LiteServerBlockLinkTlCombinator
}

private object LiteServerBlockLinkTlCombinator : TlCombinator<LiteServerBlockLink>(
    LiteServerBlockLinkBack.tlConstructor(),
    LiteServerBlockLinkForward.tlConstructor()
)