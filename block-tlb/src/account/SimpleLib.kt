package org.ton.block.account

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import kotlin.jvm.JvmStatic

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
            storeBit(value.public)
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
