package org.ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

abstract class TlbCombinator<T> : AbstractTlbCombinator<T, TlbConstructor<T>>(), TlbCodec<T> {
    abstract override val constructors: List<TlbConstructor<out T>>

    abstract override fun getConstructor(value: T): TlbConstructor<out T>

    override fun storeTlb(cellBuilder: CellBuilder, value: T) =
        storeTlbConstructor(cellBuilder, value).storeTlb(cellBuilder, value)

    override fun loadTlb(cellSlice: CellSlice): T =
        loadTlbConstructor(cellSlice).loadTlb(cellSlice)

    override fun toString(): String = this::class.simpleName.toString()
}

fun <T> TlbCombinator(tlbConstructor: TlbConstructor<T>) = object : TlbCombinator<T>() {
    override val constructors: List<TlbConstructor<out T>> = listOf(tlbConstructor)
    override fun getConstructor(value: T): TlbConstructor<out T> = tlbConstructor
}