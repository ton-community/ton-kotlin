package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.jvm.JvmStatic

@Suppress("NOTHING_TO_INLINE")
public inline fun Pair<Boolean, Boolean>.toTickTock(): TickTock = TickTock(first, second)

@SerialName("tick_tock")
@Serializable
public data class TickTock(
    val tick: Boolean,
    val tock: Boolean
) : TlbObject {
    public fun toPair(): Pair<Boolean, Boolean> = tick to tock

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("tick_tock") {
            field("tick", tick)
            field("tick", tock)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<TickTock> by TickTockTlbConstructor
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
