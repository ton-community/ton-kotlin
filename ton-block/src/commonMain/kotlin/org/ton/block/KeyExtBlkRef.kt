package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
data class KeyExtBlkRef(
    val key: Boolean,
    val blk_ref: ExtBlkRef
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<KeyExtBlkRef> = KeyExtBlkRefTlbConstructor
    }
}

private object KeyExtBlkRefTlbConstructor : TlbConstructor<KeyExtBlkRef>(
    schema = "_ key:Bool blk_ref:ExtBlkRef = KeyExtBlkRef;"
) {
    val extBlkRef by lazy { ExtBlkRef.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: KeyExtBlkRef
    ) = cellBuilder {
        storeBit(value.key)
        storeTlb(extBlkRef, value.blk_ref)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): KeyExtBlkRef = cellSlice {
        val key = loadBit()
        val blkRef = loadTlb(extBlkRef)
        KeyExtBlkRef(key, blkRef)
    }
}