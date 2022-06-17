package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("counters")
data class Counters(
    val last_updated: Long,
    val total: Long,
    val cnt2048: Long,
    val cnt65536: Long
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<Counters> = CounterTlbConstructor
    }
}

private object CounterTlbConstructor : TlbConstructor<Counters>(
    schema = "counters#_ last_updated:uint32 total:uint64 cnt2048:uint64 cnt65536:uint64 = Counters;\n"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Counters
    ) = cellBuilder {
        storeUInt(value.last_updated, 32)
        storeUInt(value.total, 64)
        storeUInt(value.cnt2048, 64)
        storeUInt(value.cnt65536, 64)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Counters = cellSlice {
        val lastUpdated = loadUInt(32).toLong()
        val total = loadUInt(64).toLong()
        val cnt2048 = loadUInt(64).toLong()
        val cnt65535 = loadUInt(64).toLong()
        Counters(lastUpdated, total, cnt2048, cnt65535)
    }
}