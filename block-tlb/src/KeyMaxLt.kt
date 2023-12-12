package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
public data class KeyMaxLt(
    val key: Boolean,
    @SerialName("max_end_lt") val maxEndLt: ULong,
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("key_max_lt") {
        field("key", key)
        field("max_end_lt", maxEndLt)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<KeyMaxLt> by KeyMaxLtTlbConstructor
}

private object KeyMaxLtTlbConstructor : TlbConstructor<KeyMaxLt>(
    schema = "_ key:Bool max_end_lt:uint64 = KeyMaxLt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: KeyMaxLt
    ) = cellBuilder {
        storeBit(value.key)
        storeUInt64(value.maxEndLt)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): KeyMaxLt = cellSlice {
        val key = loadBit()
        val maxEndLt = loadUInt64()
        KeyMaxLt(key, maxEndLt)
    }
}
