package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("counters")
data class Counters(
    val last_updated: UInt,
    val total: ULong,
    val cnt2048: ULong,
    val cnt65536: ULong
) {
    companion object : TlbConstructorProvider<Counters> by CounterTlbConstructor
}

private object CounterTlbConstructor : TlbConstructor<Counters>(
    schema = "counters#_ last_updated:uint32 total:uint64 cnt2048:uint64 cnt65536:uint64 = Counters;\n"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Counters
    ) = cellBuilder {
        storeUInt32(value.last_updated)
        storeUInt64(value.total)
        storeUInt64(value.cnt2048)
        storeUInt64(value.cnt65536)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Counters = cellSlice {
        val lastUpdated = loadUInt32()
        val total = loadUInt64()
        val cnt2048 = loadUInt64()
        val cnt65535 = loadUInt64()
        Counters(lastUpdated, total, cnt2048, cnt65535)
    }
}
