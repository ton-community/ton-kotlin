package org.ton.block.tlb

import org.ton.block.StorageUsedShort
import org.ton.block.VarUInteger
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun StorageUsedShort.Companion.tlbCodec(): TlbCodec<StorageUsedShort> = StorageUsedShortTlbConstructor()

private class StorageUsedShortTlbConstructor : TlbConstructor<StorageUsedShort>(
    schema = "storage_used_short\$_ cells:(VarUInteger 7) bits:(VarUInteger 7) = StorageUsedShort;"
) {
    private val varUInteger7Codec by lazy {
        VarUInteger.tlbCodec(7)
    }

    override fun encode(
        cellBuilder: CellBuilder, value: StorageUsedShort, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.cells, varUInteger7Codec)
        storeTlb(value.bits, varUInteger7Codec)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): StorageUsedShort = cellSlice {
        val cells = loadTlb(varUInteger7Codec)
        val bits = loadTlb(varUInteger7Codec)
        StorageUsedShort(cells, bits)
    }
}
