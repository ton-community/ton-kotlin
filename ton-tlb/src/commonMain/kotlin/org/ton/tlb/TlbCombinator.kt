package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.UnknownTlbConstructorException

abstract class AbstractTlbCombinator<T, C : AbstractTlbConstructor<out T>> {
    abstract val constructors: List<AbstractTlbConstructor<*>>

    private val sortedConstructors by lazy {
        constructors.sortedBy { it.id.size }
    }

    abstract fun getConstructor(value: T): AbstractTlbConstructor<*>

    fun storeTlbConstructor(cellBuilder: CellBuilder, value: T): C {
        val constructor = getConstructor(value)
        cellBuilder.storeBits(constructor.id)
        @Suppress("UNCHECKED_CAST")
        return constructor as C
    }

    fun loadTlbConstructor(cellSlice: CellSlice): C {
        var currentId = BitString.empty()
        val constructor = sortedConstructors.firstOrNull { constructor ->
            if (constructor.id.size > currentId.size) {
                currentId = cellSlice.preloadBits(constructor.id.size)
            }
            currentId == constructor.id
        } ?: throw UnknownTlbConstructorException()
        cellSlice.skipBits(constructor.id.size)
        @Suppress("UNCHECKED_CAST")
        return constructor as C
    }
}

fun <T> TlbCombinator(tlbConstructor: TlbConstructor<T>) = object : TlbCombinator<T>() {
    override val constructors: List<TlbConstructor<out T>> = listOf(tlbConstructor)
    override fun getConstructor(value: T): TlbConstructor<out T> = tlbConstructor
}

abstract class TlbCombinator<T> : AbstractTlbCombinator<T, TlbConstructor<T>>(), TlbCodec<T> {
    abstract override val constructors: List<TlbConstructor<out T>>

    abstract override fun getConstructor(value: T): TlbConstructor<out T>

    override fun storeTlb(cellBuilder: CellBuilder, value: T) =
        storeTlbConstructor(cellBuilder, value).storeTlb(cellBuilder, value)

    override fun loadTlb(cellSlice: CellSlice): T =
        loadTlbConstructor(cellSlice).loadTlb(cellSlice)

    override fun toString(): String = this::class.simpleName.toString()
}

abstract class TlbNegatedCombinator<T> : AbstractTlbCombinator<T, TlbNegatedConstructor<T>>(),
    TlbNegatedCodec<T> {
    abstract override val constructors: List<TlbNegatedConstructor<out T>>

    abstract override fun getConstructor(value: T): TlbNegatedConstructor<out T>

    override fun storeNegatedTlb(cellBuilder: CellBuilder, value: T): Int =
        storeTlbConstructor(cellBuilder, value).storeNegatedTlb(cellBuilder, value)

    override fun loadNegatedTlb(cellSlice: CellSlice): Pair<Int, T> =
        loadTlbConstructor(cellSlice).loadNegatedTlb(cellSlice)
}
