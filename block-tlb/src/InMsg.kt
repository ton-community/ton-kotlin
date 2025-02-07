@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")

public sealed interface InMsg : TlbObject {
    public companion object : TlbCombinatorProvider<InMsg> by InMsgTlbCombinator
}

private object InMsgTlbCombinator : TlbCombinator<InMsg>(
    InMsg::class,
    MsgImportExt::class to MsgImportExt,
    MsgImportIhr::class to MsgImportIhr,
    MsgImportImm::class to MsgImportImm,
    MsgImportFin::class to MsgImportFin,
    MsgImportTr::class to MsgImportTr,
    MsgDiscardFin::class to MsgDiscardFin,
    MsgDiscardTr::class to MsgDiscardTr,
)
