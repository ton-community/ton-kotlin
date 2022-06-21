@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface InMsg {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<InMsg> = InMsgTlbCombinator
    }
}

private object InMsgTlbCombinator : TlbCombinator<InMsg>() {
    val ext by lazy { MsgImportExt.tlbCodec() }
    val ihr by lazy { MsgImportIhr.tlbCodec() }
    val imm by lazy { MsgImportImm.tlbCodec() }
    val fin by lazy { MsgImportFin.tlbCodec() }
    val tr by lazy { MsgImportTr.tlbCodec() }
    val discardFin by lazy { MsgDiscardFin.tlbCodec() }
    val discardTr by lazy { MsgDiscardTr.tlbCodec() }

    override val constructors: List<TlbConstructor<out InMsg>> by lazy {
        listOf(
            ext, ihr, imm, fin, tr, discardFin, discardTr
        )
    }

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