package org.ton.block.tlb

import org.ton.block.StorageUsed
import org.ton.block.VarUInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun StorageUsed.Companion.tlbCodec(): TlbCodec<StorageUsed> = StorageUsedTlbConstructor()

private class StorageUsedTlbConstructor : TlbConstructor<StorageUsed>(
    schema = "storage_used$_ cells:(VarUInteger 7) bits:(VarUInteger 7) public_cells:(VarUInteger 7) = StorageUsed;"
) {
    private val varUInteger7Codec = VarUInteger.tlbCodec(7)

    override fun encode(
        cellBuilder: CellBuilder, value: StorageUsed, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.cells, varUInteger7Codec)
        storeTlb(value.bits, varUInteger7Codec)
        storeTlb(value.publicCells, varUInteger7Codec)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): StorageUsed = cellSlice {
        val cells = loadTlb(varUInteger7Codec)
        val bits = loadTlb(varUInteger7Codec)
        val publicCells = loadTlb(varUInteger7Codec)
        StorageUsed(cells, bits, publicCells)
    }
}
