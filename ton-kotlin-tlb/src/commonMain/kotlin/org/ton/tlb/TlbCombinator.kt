package org.ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.providers.TlbCombinatorProvider
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

abstract class TlbCombinator<T : Any> : AbstractTlbCombinator<T, TlbConstructor<T>>(), TlbCodec<T>,
    TlbCombinatorProvider<T> {
    abstract override val constructors: List<TlbConstructor<out T>>

    override fun tlbCombinator(): TlbCombinator<T> = this

    @Suppress("UNCHECKED_CAST")
    fun getConstructor(type: KType): TlbConstructor<T> {
        val constructor = checkNotNull(
            constructors.find { constructor ->
                constructor.type?.isSupertypeOf(type) == true
            }
        ) {
            "Invalid type. actual: $type"
        }
        return constructor as TlbConstructor<T>
    }

    override fun getConstructor(value: T): TlbConstructor<out T> = getConstructor(value::class.createType())

    override fun storeTlb(cellBuilder: CellBuilder, value: T) =
        storeTlbConstructor(cellBuilder, value).storeTlb(cellBuilder, value)

    override fun loadTlb(cellSlice: CellSlice): T =
        loadTlbConstructor(cellSlice).loadTlb(cellSlice)

    override fun toString(): String = this::class.simpleName.toString()
}

fun <T : Any> TlbCombinator(vararg tlbConstructor: TlbConstructor<T>) = object : TlbCombinator<T>() {
    override val constructors: List<TlbConstructor<out T>> = tlbConstructor.sortedBy { it.id }
}

fun <T : Any> TlbCombinator(iterable: Iterable<TlbConstructor<out T>>) = object : TlbCombinator<T>() {
    override val constructors: List<TlbConstructor<out T>> = iterable.sortedBy { it.id }
}