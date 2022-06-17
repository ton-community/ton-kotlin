package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
enum class AccStatusChange {
    @SerialName("acst_unchanged")
    UNCHANGED, // x -> x

    @SerialName("acst_frozen")
    FROZEN, // init -> frozen

    @SerialName("acst_deleted")
    DELETED // frozen -> deleted
    ;

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<AccStatusChange> = AccStatusChangeTlbCombinator
    }
}

private object AccStatusChangeTlbCombinator : TlbCombinator<AccStatusChange>() {
    override val constructors: List<TlbConstructor<out AccStatusChange>> = listOf(
        AccStatusChangeUnchangedTlbConstructor,
        AccStatusChangeFrozenTlbConstructor,
        AccStatusChangeDeletedTlbConstructor
    )

    override fun getConstructor(
        value: AccStatusChange
    ): TlbConstructor<out AccStatusChange> = when (value) {
        AccStatusChange.UNCHANGED -> AccStatusChangeUnchangedTlbConstructor
        AccStatusChange.FROZEN -> AccStatusChangeFrozenTlbConstructor
        AccStatusChange.DELETED -> AccStatusChangeDeletedTlbConstructor
    }
}

private object AccStatusChangeUnchangedTlbConstructor : TlbConstructor<AccStatusChange>(
    schema = "acst_unchanged\$0 = AccStatusChange;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccStatusChange) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccStatusChange = AccStatusChange.UNCHANGED
}

private object AccStatusChangeFrozenTlbConstructor : TlbConstructor<AccStatusChange>(
    schema = "acst_frozen\$10 = AccStatusChange;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccStatusChange) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccStatusChange = AccStatusChange.FROZEN
}

private object AccStatusChangeDeletedTlbConstructor : TlbConstructor<AccStatusChange>(
    schema = "acst_deleted\$11 = AccStatusChange;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccStatusChange) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccStatusChange = AccStatusChange.DELETED
}