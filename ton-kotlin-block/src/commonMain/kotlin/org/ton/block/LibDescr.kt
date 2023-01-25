package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.hashmap.HashMapEdge
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("shared_lib_descr")
public data class LibDescr(
    val lib: Cell,
    val publishers: HashMapEdge<Unit>
) {
    public companion object : TlbConstructorProvider<LibDescr> by LibDescrTlbConstructor
}

private object LibDescrTlbConstructor : TlbConstructor<LibDescr>(
    schema = "shared_lib_descr\$00 lib:^Cell publishers:(Hashmap 256 True) = LibDescr;"
) {
    val publishers by lazy {
        HashMapEdge.tlbCodec(256, object : TlbCodec<Unit> {
            override fun storeTlb(cellBuilder: CellBuilder, value: Unit) {
            }

            override fun loadTlb(cellSlice: CellSlice) {
            }
        })
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: LibDescr
    ) = cellBuilder {
        storeRef(value.lib)
        storeTlb(publishers, value.publishers)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): LibDescr = cellSlice {
        val lib = loadRef()
        val publishers = loadTlb(publishers)
        LibDescr(lib, publishers)
    }
}
