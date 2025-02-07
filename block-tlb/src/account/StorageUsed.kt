package org.ton.kotlin.account

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * Amount of unique cells and bits for shard states.
 */
public data class StorageUsed(
    /**
     * Amount of unique cells.
     */
    val cells: Long = 0,

    /**
     * The total number of bits in unique cells.
     */
    val bits: Long = 0,

    /**
     * The number of public libraries in the state.
     *
     * NOTE: Currently not used
     */
    val publicCells: Long = 0,
) {
    public companion object {
        /**
         * The additive identity for this type, i. e. 0.
         */
        public val ZERO: StorageUsed = StorageUsed(0L, 0L, 0L)
    }
}

private object StorageUsedSerializer : CellSerializer<StorageUsed> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): StorageUsed {
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: StorageUsed,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}
/*
    public object Tlb : TlbCodec<StorageUsed> {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: StorageUsed
        ): Unit = cellBuilder {
            storeVarUInt(value.cells, 7)
            storeVarUInt(value.bits, 7)
            storeVarUInt(value.publicCells, 7)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): StorageUsed = cellSlice {
            val cells = loadVarUInt(7).toLong()
            val bits = loadVarUInt(7).toLong()
            val publicCells = loadVarUInt(7).toLong()
            StorageUsed(cells, bits, publicCells)
        }
    }

 */