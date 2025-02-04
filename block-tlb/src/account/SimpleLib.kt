package org.ton.block.account

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec

/**
 * Simple TVM library.
 */
public data class SimpleLib(
    /**
     * Whether this library is accessible from other accounts.
     */
    val public: Boolean,

    /**
     * Library code.
     */
    val root: Cell
) {
    public companion object Tlb : TlbCodec<SimpleLib> {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: SimpleLib
        ): Unit = cellBuilder {
            storeBoolean(value.public)
            storeRef(value.root)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): SimpleLib = cellSlice {
            val public = loadBit()
            val root = loadRef()
            SimpleLib(public, root)
        }
    }
}
