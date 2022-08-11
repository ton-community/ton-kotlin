package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
data class KeyMaxLt(
    val key: Boolean,
    val max_end_lt: Long,
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<KeyMaxLt> = KeyMaxLtTlbConstructor
    }
}

private object KeyMaxLtTlbConstructor : TlbConstructor<KeyMaxLt>(
    schema = "_ key:Bool max_end_lt:uint64 = KeyMaxLt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: KeyMaxLt
    ) = cellBuilder {
        storeBit(value.key)
        storeUInt(value.max_end_lt, 64)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): KeyMaxLt = cellSlice {
        val key = loadBit()
        val maxEndLt = loadUInt(64).toLong()
        KeyMaxLt(key, maxEndLt)
    }
}