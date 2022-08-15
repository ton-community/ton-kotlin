package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
data class KeyMaxLt(
    val key: Boolean,
    val max_end_lt: ULong,
) {
    companion object : TlbConstructorProvider<KeyMaxLt> by KeyMaxLtTlbConstructor
}

private object KeyMaxLtTlbConstructor : TlbConstructor<KeyMaxLt>(
    schema = "_ key:Bool max_end_lt:uint64 = KeyMaxLt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: KeyMaxLt
    ) = cellBuilder {
        storeBit(value.key)
        storeUInt64(value.max_end_lt)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): KeyMaxLt = cellSlice {
        val key = loadBit()
        val maxEndLt = loadUInt64()
        KeyMaxLt(key, maxEndLt)
    }
}
