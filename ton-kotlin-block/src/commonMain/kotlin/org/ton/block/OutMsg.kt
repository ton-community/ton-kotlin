@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface OutMsg : TlbObject {
    public companion object : TlbCombinatorProvider<OutMsg> by OutMsgTlbCombinator
}

private object OutMsgTlbCombinator : TlbCombinator<OutMsg>(
    OutMsg::class,
    MsgExportExt::class to MsgExportExt,
    MsgExportImm::class to MsgExportImm,
    MsgExportNew::class to MsgExportNew,
    MsgExportTr::class to MsgExportTr,
    MsgExportDeq::class to MsgExportDeq,
    MsgExportDeqShort::class to MsgExportDeqShort,
    MsgExportTrReq::class to MsgExportTrReq,
    MsgExportDeqImm::class to MsgExportDeqImm,
)
