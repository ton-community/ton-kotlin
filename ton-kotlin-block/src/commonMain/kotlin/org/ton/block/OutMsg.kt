@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface OutMsg {
    companion object : TlbCombinatorProvider<OutMsg> by OutMsgTlbCombinator
}

private object OutMsgTlbCombinator : TlbCombinator<OutMsg>() {
    val msgExportExt = MsgExportExt.tlbConstructor()
    val msgExportImm = MsgExportImm.tlbConstructor()
    val msgExportNew = MsgExportNew.tlbConstructor()
    val msgExportTr = MsgExportTr.tlbConstructor()
    val msgExportDeq = MsgExportDeq.tlbConstructor()
    val msgExportDeqShort = MsgExportDeqShort.tlbConstructor()
    val msgExportTrReq = MsgExportTrReq.tlbConstructor()
    val msgExportDeqImm = MsgExportDeqImm.tlbConstructor()

    override val constructors: List<TlbConstructor<out OutMsg>> = listOf(
        msgExportExt,
        msgExportImm,
        msgExportNew,
        msgExportTr,
        msgExportDeq,
        msgExportDeqShort,
        msgExportTrReq,
        msgExportDeqImm
    )

    override fun getConstructor(
        value: OutMsg
    ): TlbConstructor<out OutMsg> = when (value) {
        is MsgExportExt -> msgExportExt
        is MsgExportImm -> msgExportImm
        is MsgExportNew -> msgExportNew
        is MsgExportTr -> msgExportTr
        is MsgExportDeq -> msgExportDeq
        is MsgExportDeqShort -> msgExportDeqShort
        is MsgExportTrReq -> msgExportTrReq
        is MsgExportDeqImm -> msgExportDeqImm
    }
}
