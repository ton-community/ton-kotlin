package org.ton.tlb

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.UnknownTlbConstructorException

abstract class TlbCombinator<T : Any>(
    open val constructors: List<TlbConstructor<out T>>
) : TlbCodec<T>() {
    private val sortedConstructors by lazy {
        constructors.asSequence().sortedBy { it.id.length }
    }

    abstract fun getConstructor(value: T): TlbConstructor<out T>

    @Suppress("UNCHECKED_CAST")
    override fun encode(cellBuilder: CellBuilder, value: T, param: Int, negativeParam: (Int) -> Unit) {
        val constructor = getConstructor(value) as TlbConstructor<T>
        cellBuilder.storeBits(constructor.id)
        constructor.encode(cellBuilder, value, param, negativeParam)
    }

    override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): T {
        val constructor = sortedConstructors.find { constructor ->
            val id = cellSlice.preloadBitString(constructor.id.length)
            id == constructor.id
        } ?: throw UnknownTlbConstructorException()
        return constructor.decode(cellSlice, param, negativeParam)
    }

    constructor(vararg constructors: TlbConstructor<out T>) : this(constructors.toList())

    override fun toString(): String = constructors.joinToString("\n")
}
