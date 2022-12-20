package org.ton.tlb

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import kotlin.jvm.JvmStatic

public inline fun <T> CellRef(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(cell, codec)
public inline fun <T> CellRef(value: T, codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(value, codec)

public inline fun <T> Cell.asRef(codec: TlbCodec<T>): CellRef<T> = CellRef.valueOf(this, codec)

public interface CellRef<T> {
    public val cell: Cell
    public val codec: TlbCodec<T>
    public val value: T

    public operator fun getValue(thisRef: Any?, property: Any?): T = value

    public companion object {
        @JvmStatic
        public fun <T> valueOf(cell: Cell, codec: TlbCodec<T>): CellRef<T> = CellRefImpl(cell, codec)

        @JvmStatic
        public fun <T> valueOf(value: T, codec: TlbCodec<T>): CellRef<T> = CellRefValue(value, codec)

        @JvmStatic
        public fun <T> tlbCodec(codec: TlbCodec<T>): TlbCodec<CellRef<T>> = CellRefTlbConstructor(codec)
    }
}

private class CellRefImpl<T>(
    override val cell: Cell,
    override val codec: TlbCodec<T>
) : CellRef<T> {
    override val value: T by lazy(LazyThreadSafetyMode.PUBLICATION) {
        cell.parse(codec)
    }
}

private class CellRefValue<T>(
    override val value: T,
    override val codec: TlbCodec<T>
) : CellRef<T> {
    override val cell: Cell by lazy(LazyThreadSafetyMode.PUBLICATION) {
        CellBuilder.createCell {
            storeTlb(codec, value)
        }
    }
}

private class CellRefTlbConstructor<T>(
    val codec: TlbCodec<T>
) : TlbCodec<CellRef<T>> {
    override fun storeTlb(cellBuilder: CellBuilder, value: CellRef<T>) {
        cellBuilder.storeRef(value.cell)
    }

    override fun loadTlb(cellSlice: CellSlice): CellRef<T> {
        return cellSlice.loadRef().asRef(codec)
    }
}
