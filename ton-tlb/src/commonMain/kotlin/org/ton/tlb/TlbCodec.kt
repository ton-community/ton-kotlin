package org.ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

fun interface TlbStorer<T> {
    fun storeTlb(cellBuilder: CellBuilder, value: T)
}

fun interface TlbNegatedStorer<T> : TlbStorer<T> {
    fun storeNegatedTlb(cellBuilder: CellBuilder, value: T): Int

    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        storeNegatedTlb(cellBuilder, value)
    }
}

fun interface TlbLoader<T> {
    fun loadTlb(cellSlice: CellSlice): T
}

fun interface TlbNegatedLoader<T> : TlbLoader<T> {
    fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, T>

    override fun loadTlb(cellSlice: CellSlice): T = loadNegatedTlb(cellSlice).second
}

interface TlbCodec<T> : TlbStorer<T>, TlbLoader<T>
interface TlbNegatedCodec<T> : TlbCodec<T>, TlbNegatedStorer<T>, TlbNegatedLoader<T>

fun <T : Any> CellSlice.loadTlb(codec: TlbLoader<T>): T {
    return codec.loadTlb(this)
}

fun <T : Any> CellSlice.loadNegatedTlb(codec: TlbNegatedLoader<T>): Pair<Int, T> {
    return codec.loadNegatedTlb(this)
}

fun <T : Any> CellBuilder.storeTlb(codec: TlbStorer<T>, value: T) = apply {
    codec.storeTlb(this, value)
}

fun <T : Any> CellBuilder.storeNegatedTlb(codec: TlbNegatedStorer<T>, value: T) =
    codec.storeNegatedTlb(this, value)
