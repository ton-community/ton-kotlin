package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("storage_used")
@Serializable
data class StorageUsed(
    val cells: VarUInteger,
    val bits: VarUInteger,
    @SerialName("public_cells")
    val publicCells: VarUInteger,
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<StorageUsed> = StorageUsedTlbConstructor()
    }
}

private class StorageUsedTlbConstructor : TlbConstructor<StorageUsed>(
    schema = "storage_used\$_ cells:(VarUInteger 7) bits:(VarUInteger 7) public_cells:(VarUInteger 7) = StorageUsed;"
) {
    private val varUInteger7Codec by lazy {
        VarUInteger.tlbCodec(7)
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: StorageUsed
    ) = cellBuilder {
        storeTlb(varUInteger7Codec, value.cells)
        storeTlb(varUInteger7Codec, value.bits)
        storeTlb(varUInteger7Codec, value.publicCells)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): StorageUsed = cellSlice {
        val cells = loadTlb(varUInteger7Codec)
        val bits = loadTlb(varUInteger7Codec)
        val publicCells = loadTlb(varUInteger7Codec)
        StorageUsed(cells, bits, publicCells)
    }
}
