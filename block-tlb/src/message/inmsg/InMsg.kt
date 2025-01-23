@file:Suppress("OPT_IN_USAGE")

package org.ton.block.message.inmsg

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@Serializable
public sealed interface InMsg : TlbObject {
    public companion object : TlbCombinatorProvider<InMsg> by InMsgTlbCombinator
}

private object InMsgTlbCombinator : TlbCombinator<InMsg>(
    InMsg::class,
    InMsgExternal::class to InMsgExternal,
    InMsgIhr::class to InMsgIhr,
    InMsgImmediate::class to InMsgImmediate,
    InMsgFinal::class to InMsgFinal,
    InMsgTransit::class to InMsgTransit,
    MsgDiscardFinal::class to MsgDiscardFinal.Companion,
    MsgDiscardTransit::class to MsgDiscardTransit.Companion,
)
