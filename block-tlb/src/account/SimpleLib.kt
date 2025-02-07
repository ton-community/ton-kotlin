package org.ton.kotlin.account

import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

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
    //    public companion object Tlb : TlbCodec<SimpleLib> {
//        override fun storeTlb(
//            cellBuilder: CellBuilder, value: SimpleLib
//        ): Unit = cellBuilder {
//            storeBoolean(value.public)
//            storeRef(value.root)
//        }
//
//        override fun loadTlb(
//            cellSlice: CellSlice
//        ): SimpleLib = cellSlice {
//            val public = loadBit()
//            val root = loadRef()
//            SimpleLib(public, root)
//        }
//    }
    public companion object : CellSerializer<SimpleLib> by SimpleLibSerializer
}

private object SimpleLibSerializer : CellSerializer<SimpleLib> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): SimpleLib {
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: SimpleLib,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}
