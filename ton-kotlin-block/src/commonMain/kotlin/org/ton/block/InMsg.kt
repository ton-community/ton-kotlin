@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")
@Serializable
sealed interface InMsg {
    companion object : TlbCombinatorProvider<InMsg> by InMsgTlbCombinator
}

private object InMsgTlbCombinator : TlbCombinator<InMsg>() {
    val ext = MsgImportExt.tlbConstructor()
    val ihr = MsgImportIhr.tlbConstructor()
    val imm = MsgImportImm.tlbConstructor()
    val fin = MsgImportFin.tlbConstructor()
    val tr = MsgImportTr.tlbConstructor()
    val discardFin = MsgDiscardFin.tlbConstructor()
    val discardTr = MsgDiscardTr.tlbConstructor()

    override val constructors: List<TlbConstructor<out InMsg>> =
        listOf(
            ext, ihr, imm, fin, tr, discardFin, discardTr
        )

    override fun getConstructor(
        value: InMsg
    ): TlbConstructor<out InMsg> = when (value) {
        is MsgDiscardFin -> discardFin
        is MsgDiscardTr -> discardTr
        is MsgImportExt -> ext
        is MsgImportFin -> fin
        is MsgImportIhr -> ihr
        is MsgImportImm -> imm
        is MsgImportTr -> tr
    }
}
