package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
enum class AccStatusChange {
    @SerialName("acst_unchanged")
    UNCHANGED {
        override fun toString(): String = "acst_unchanged"
    }, // x -> x

    @SerialName("acst_frozen")
    FROZEN {
        override fun toString(): String = "acst_frozen"
    }, // init -> frozen

    @SerialName("acst_deleted")
    DELETED {
        override fun toString(): String = "acst_deleted"
    } // frozen -> deleted
    ;

    companion object : TlbCodec<AccStatusChange> by AccStatusChangeTlbCombinator
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