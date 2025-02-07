package org.ton.kotlin.account

/**
 * Amount of unique cells and bits.
 */
public data class StorageUsedShort(
    /**
     * Amount of unique cells.
     */
    val cells: Long = 0,

    /**
     * The total number of bits in unique cells.
     */
    val bits: Long = 0,
) {
    public companion object {
        /**
         * The additive identity for this type, i. e. 0.
         */
        public val ZERO: StorageUsedShort = StorageUsedShort(0, 0)
    }
}

//public object Tlb : TlbCodec<StorageUsedShort> {
//    override fun storeTlb(
//        cellBuilder: CellBuilder, value: StorageUsedShort
//    ): Unit = cellBuilder {
//        storeVarUInt(value.cells, 7)
//        storeVarUInt(value.bits, 7)
//    }
//
//    override fun loadTlb(
//        cellSlice: CellSlice
//    ): StorageUsedShort = cellSlice {
//        val cells = loadVarUInt(7).toLong()
//        val bits = loadVarUInt(7).toLong()
//        StorageUsedShort(cells, bits)
//    }
//}

