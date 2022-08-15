package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
data class KeyExtBlkRef(
    val key: Boolean,
    val blk_ref: ExtBlkRef
) {
    companion object : TlbConstructorProvider<KeyExtBlkRef> by KeyExtBlkRefTlbConstructor
}

private object KeyExtBlkRefTlbConstructor : TlbConstructor<KeyExtBlkRef>(
    schema = "_ key:Bool blk_ref:ExtBlkRef = KeyExtBlkRef;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: KeyExtBlkRef
    ) = cellBuilder {
        storeBit(value.key)
        storeTlb(ExtBlkRef, value.blk_ref)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): KeyExtBlkRef = cellSlice {
        val key = loadBit()
        val blkRef = loadTlb(ExtBlkRef)
        KeyExtBlkRef(key, blkRef)
    }
}
