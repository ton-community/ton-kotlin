package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@Serializable
data class SimpleLib(
    val public: Boolean,
    val root: Cell
) {
    companion object : TlbCodec<SimpleLib> by SimpleLibTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<SimpleLib> = SimpleLibTlbConstructor
    }
}

private object SimpleLibTlbConstructor : TlbConstructor<SimpleLib>(
    schema = "simple_lib\$_ public:Bool root:^Cell = SimpleLib;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: SimpleLib
    ) = cellBuilder {
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
