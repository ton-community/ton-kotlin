package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.*
import org.ton.tlb.TlbConstructor

@Serializable
data class SimpleLib(
    val public: Boolean,
    val root: Cell
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<SimpleLib> = SimpleLibTlbConstructor()
    }
}

private class SimpleLibTlbConstructor : TlbConstructor<SimpleLib>(
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
