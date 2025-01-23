@file:Suppress("OPT_IN_USAGE")

package org.ton.block.message.export

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable

public sealed interface OutMsg : TlbObject {
    public companion object : TlbCombinatorProvider<OutMsg> by OutMsgTlbCombinator
}

private object OutMsgTlbCombinator : TlbCombinator<OutMsg>(
    OutMsg::class,
    OutMsgExternal::class to OutMsgExternal,
    OutMsgImmediate::class to OutMsgImmediate,
    OutMsgNew::class to OutMsgNew,
    OutMsgTransit::class to OutMsgTransit,
    OutMsgDeque::class to OutMsgDeque,
    OutMsgDequeShort::class to OutMsgDequeShort,
    OutMsgTransitRequest::class to OutMsgTransitRequest,
    OutMsgDequeImmediate::class to OutMsgDequeImmediate,
)
