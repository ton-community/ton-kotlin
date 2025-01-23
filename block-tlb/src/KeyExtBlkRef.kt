package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.block.block.BlockRef
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

public data class KeyExtBlkRef(
    val key: Boolean, // key: Bool
    @SerialName("blk_ref") val blkRef: BlockRef // blk_ref: ExtBlkRef
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return printer.type {
            field("key", key)
            field("blk_ref", blkRef)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<KeyExtBlkRef> by KeyExtBlkRefTlbConstructor
}

private object KeyExtBlkRefTlbConstructor : TlbConstructor<KeyExtBlkRef>(
    schema = "_ key:Bool blk_ref:ExtBlkRef = KeyExtBlkRef;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: KeyExtBlkRef
    ) = cellBuilder {
        storeBit(value.key)
        storeTlb(BlockRef.Tlb, value.blkRef)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): KeyExtBlkRef = cellSlice {
        val key = loadBit()
        val blkRef = loadTlb(BlockRef.Tlb)
        KeyExtBlkRef(key, blkRef)
    }
}
