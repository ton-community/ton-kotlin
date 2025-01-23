package org.ton.block.transaction.phases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbStorer
import kotlin.jvm.JvmStatic

@Serializable
public enum class ComputePhaseSkipReason {
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

    public companion object : TlbCodec<ComputePhaseSkipReason> by ComputeSkipReasonTlbCombinator {
        @JvmStatic
        public fun tlbCodec(): TlbCombinator<ComputePhaseSkipReason> = ComputeSkipReasonTlbCombinator
    }
}

private object ComputeSkipReasonTlbCombinator : TlbCombinator<ComputePhaseSkipReason>(
    ComputePhaseSkipReason::class,
    ComputePhaseSkipReason::class to ComputeSkipReasonNoStateTlbConstructor,
    ComputePhaseSkipReason::class to ComputeSkipReasonBadStateTlbConstructor,
    ComputePhaseSkipReason::class to ComputeSkipReasonNoGasTlbConstructor,
) {
    override fun findTlbStorerOrNull(value: ComputePhaseSkipReason): TlbStorer<ComputePhaseSkipReason>? {
        return when (value) {
            ComputePhaseSkipReason.NO_STATE -> ComputeSkipReasonNoStateTlbConstructor
            ComputePhaseSkipReason.BAD_STATE -> ComputeSkipReasonBadStateTlbConstructor
            ComputePhaseSkipReason.NO_GAS -> ComputeSkipReasonNoGasTlbConstructor
        }
    }
}

private object ComputeSkipReasonNoStateTlbConstructor : TlbConstructor<ComputePhaseSkipReason>(
    schema = "cskip_no_state\$00 = ComputeSkipReason;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ComputePhaseSkipReason
    ) = Unit

    override fun loadTlb(cellSlice: CellSlice): ComputePhaseSkipReason = ComputePhaseSkipReason.NO_STATE
}

private object ComputeSkipReasonBadStateTlbConstructor : TlbConstructor<ComputePhaseSkipReason>(
    schema = "cskip_bad_state\$01 = ComputeSkipReason;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ComputePhaseSkipReason
    ) = Unit

    override fun loadTlb(cellSlice: CellSlice): ComputePhaseSkipReason = ComputePhaseSkipReason.BAD_STATE
}

private object ComputeSkipReasonNoGasTlbConstructor : TlbConstructor<ComputePhaseSkipReason>(
    schema = "cskip_no_gas\$10 = ComputeSkipReason;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ComputePhaseSkipReason
    ) = Unit

    override fun loadTlb(cellSlice: CellSlice): ComputePhaseSkipReason = ComputePhaseSkipReason.NO_GAS
}
