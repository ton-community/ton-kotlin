package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
enum class ComputeSkipReason {
    @SerialName("cskip_no_state")
    NO_STATE {
        override fun toString(): String = "cskip_no_state"
    },

    @SerialName("cskip_bad_state")
    BAD_STATE {
        override fun toString(): String = "cskip_bad_state"
    },

    @SerialName("cskip_no_gas")
    NO_GAS {
        override fun toString(): String = "cskip_bad_state"
    }
    ;

    companion object : TlbCodec<ComputeSkipReason> by ComputeSkipReasonTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<ComputeSkipReason> = ComputeSkipReasonTlbCombinator
    }
}

private object ComputeSkipReasonTlbCombinator : TlbCombinator<ComputeSkipReason>() {
    override val constructors: List<TlbConstructor<out ComputeSkipReason>> = listOf(
        ComputeSkipReasonNoStateTlbConstructor,
        ComputeSkipReasonBadStateTlbConstructor,
        ComputeSkipReasonNoGasTlbConstructor
    )

    override fun getConstructor(
        value: ComputeSkipReason
    ): TlbConstructor<out ComputeSkipReason> = when (value) {
        ComputeSkipReason.NO_STATE -> ComputeSkipReasonNoStateTlbConstructor
        ComputeSkipReason.BAD_STATE -> ComputeSkipReasonBadStateTlbConstructor
        ComputeSkipReason.NO_GAS -> ComputeSkipReasonNoGasTlbConstructor
    }
}

private object ComputeSkipReasonNoStateTlbConstructor : TlbConstructor<ComputeSkipReason>(
    schema = "cskip_no_state\$00 = ComputeSkipReason;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ComputeSkipReason
    ) = Unit

    override fun loadTlb(cellSlice: CellSlice): ComputeSkipReason = ComputeSkipReason.NO_STATE
}

private object ComputeSkipReasonBadStateTlbConstructor : TlbConstructor<ComputeSkipReason>(
    schema = "cskip_bad_state\$01 = ComputeSkipReason;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ComputeSkipReason
    ) = Unit

    override fun loadTlb(cellSlice: CellSlice): ComputeSkipReason = ComputeSkipReason.BAD_STATE
}

private object ComputeSkipReasonNoGasTlbConstructor : TlbConstructor<ComputeSkipReason>(
    schema = "cskip_no_gas\$10 = ComputeSkipReason;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ComputeSkipReason
    ) = Unit

    override fun loadTlb(cellSlice: CellSlice): ComputeSkipReason = ComputeSkipReason.NO_GAS
}