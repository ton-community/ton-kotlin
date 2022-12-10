package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@SerialName("storage_used")
@Serializable
data class StorageUsed(
    val cells: VarUInteger,
    val bits: VarUInteger,
    val public_cells: VarUInteger,
) {
    companion object : TlbCodec<StorageUsed> by StorageUsedTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<StorageUsed> = StorageUsedTlbConstructor
    }

    override fun toString() = "storage_used(cells:$cells bits:$bits public_cells:$public_cells)"
}

private object StorageUsedTlbConstructor : TlbConstructor<StorageUsed>(
    schema = "storage_used\$_ cells:(VarUInteger 7) bits:(VarUInteger 7) public_cells:(VarUInteger 7) = StorageUsed;"
) {
    private val varUInteger7 = VarUInteger.tlbCodec(7)

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StorageUsed
    ) = cellBuilder {
        storeTlb(varUInteger7, value.cells)
        storeTlb(varUInteger7, value.bits)
        storeTlb(varUInteger7, value.public_cells)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StorageUsed = cellSlice {
        val cells = loadTlb(varUInteger7)
        val bits = loadTlb(varUInteger7)
        val publicCells = loadTlb(varUInteger7)
        StorageUsed(cells, bits, publicCells)
    }
}
