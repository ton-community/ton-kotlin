package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("storage_used_short")
@Serializable
data class StorageUsedShort(
    val cells: VarUInteger,
    val bits: VarUInteger
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<StorageUsedShort> = StorageUsedShortTlbConstructor()
    }
}

private class StorageUsedShortTlbConstructor : TlbConstructor<StorageUsedShort>(
    schema = "storage_used_short\$_ cells:(VarUInteger 7) bits:(VarUInteger 7) = StorageUsedShort;"
) {
    private val varUInteger7Codec by lazy {
        VarUInteger.tlbCodec(7)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StorageUsedShort
    ) = cellBuilder {
        storeTlb(varUInteger7Codec, value.cells)
        storeTlb(varUInteger7Codec, value.bits)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StorageUsedShort = cellSlice {
        val cells = loadTlb(varUInteger7Codec)
        val bits = loadTlb(varUInteger7Codec)
        StorageUsedShort(cells, bits)
    }
}
