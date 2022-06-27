package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@Suppress("NOTHING_TO_INLINE")
inline fun Pair<Boolean, Boolean>.toTickTock(): TickTock = TickTock(first, second)

@SerialName("tick_tock")
@Serializable
data class TickTock(
    val tick: Boolean,
    val tock: Boolean
) {
    fun toPair(): Pair<Boolean, Boolean> = tick to tock

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCodec<TickTock> = TickTockTlbConstructor
    }
}

private object TickTockTlbConstructor : TlbConstructor<TickTock>(
    schema = "tick_tock\$_ tick:Bool tock:Bool = TickTock;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: TickTock
    ) = cellBuilder {
        storeBit(value.tick)
        storeBit(value.tock)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): TickTock = cellSlice {
        val tick = loadBit()
        val tock = loadBit()
        TickTock(tick, tock)
    }
}
