package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("fsm_none")
public object FutureSplitMergeNone : FutureSplitMerge,
    TlbConstructorProvider<FutureSplitMergeNone> by FutureSplitMergeNoneTlbConstructor {
    override fun toString(): String = "fsm_none"
}

private object FutureSplitMergeNoneTlbConstructor : TlbConstructor<FutureSplitMergeNone>(
    schema = "fsm_none\$0 = FutureSplitMerge;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: FutureSplitMergeNone) = Unit
    override fun loadTlb(cellSlice: CellSlice): FutureSplitMergeNone = FutureSplitMergeNone
}
