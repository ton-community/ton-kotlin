@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface OutMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<OutMsg> = OutMsgTlbCombinator
    }
}

private object OutMsgTlbCombinator : TlbCombinator<OutMsg>() {
    override val constructors: List<TlbConstructor<out OutMsg>> = listOf(
        MsgExportExt.tlbCodec(),
        MsgExportImm.tlbCodec(),
        MsgExportNew.tlbCodec(),
        MsgExportTr.tlbCodec(),
        MsgExportDeq.tlbCodec(),
        MsgExportDeqShort.tlbCodec(),
        MsgExportTrReq.tlbCodec(),
        MsgExportDeqImm.tlbCodec()
    )

    override fun getConstructor(
        value: OutMsg
    ): TlbConstructor<out OutMsg> = when (value) {
        is MsgExportDeq -> MsgExportDeq.tlbCodec()
        is MsgExportDeqImm -> MsgExportDeqImm.tlbCodec()
        is MsgExportDeqShort -> MsgExportDeqShort.tlbCodec()
        is MsgExportExt -> MsgExportExt.tlbCodec()
        is MsgExportImm -> MsgExportImm.tlbCodec()
        is MsgExportNew -> MsgExportNew.tlbCodec()
        is MsgExportTr -> MsgExportTr.tlbCodec()
        is MsgExportTrReq -> MsgExportTrReq.tlbCodec()
    }
}