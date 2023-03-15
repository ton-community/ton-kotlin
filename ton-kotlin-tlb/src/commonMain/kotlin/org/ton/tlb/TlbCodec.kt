@file:Suppress("NOTHING_TO_INLINE")

package org.ton.tlb

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

public interface TlbStorer<in T> {
    public fun storeTlb(cellBuilder: CellBuilder, value: T)
    public fun createCell(value: T): Cell = CellBuilder.createCell {
        storeTlb(this, value)
    }
}

public interface TlbNegatedStorer<T> : TlbStorer<T> {
    public fun storeNegatedTlb(cellBuilder: CellBuilder, value: T): Int

    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        storeNegatedTlb(cellBuilder, value)
    }
}

public interface TlbLoader<T> {
    public fun loadTlb(cell: Cell): T {
        val cellSlice = cell.beginParse()
        return loadTlb(cellSlice)
    }

    public fun loadTlb(cellSlice: CellSlice): T
}

public interface TlbNegatedLoader<T> : TlbLoader<T> {
    public fun loadNegatedTlb(cell: Cell): TlbNegatedResult<T> = cell.parse {
        loadNegatedTlb(this)
    }

    public fun loadNegatedTlb(cellSlice: CellSlice): TlbNegatedResult<T>

    override fun loadTlb(cellSlice: CellSlice): T = loadNegatedTlb(cellSlice).value
}

public data class TlbNegatedResult<T>(
    val num: Int,
    val value: T
)

public interface TlbCodec<T> : TlbStorer<T>, TlbLoader<T>
public interface TlbNegatedCodec<T> : TlbCodec<T>, TlbNegatedStorer<T>, TlbNegatedLoader<T>

public inline fun <T> CellSlice.loadTlb(codec: TlbLoader<T>): T {
    return codec.loadTlb(this)
}

public inline fun <T> CellSlice.loadNegatedTlb(codec: TlbNegatedLoader<T>): TlbNegatedResult<T> {
    return codec.loadNegatedTlb(this)
}

public inline fun <T> CellBuilder.storeTlb(codec: TlbStorer<T>, value: T): CellBuilder = apply {
    codec.storeTlb(this, value)
}

public inline fun <T> CellBuilder.storeNegatedTlb(codec: TlbNegatedStorer<T>, value: T): Int =
    codec.storeNegatedTlb(this, value)
