package org.ton.tlb

import kotlinx.atomicfu.atomic
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.UnknownTlbConstructorException

abstract class AbstractTlbCombinator<T : Any, C : AbstractTlbConstructor<out T>> {
    companion object {
        var EXPERIMENTAL_BINARY_TREE_SEARCH by atomic(true)
    }

    abstract val constructors: List<AbstractTlbConstructor<*>>

    private val sortedConstructors by lazy {
        constructors.sortedBy { it.id.size }
    }

    private val tlbConstructorTree by lazy {
        TlbConstructorTree().apply {
            constructors.sortedBy { it.id }.forEach { constructor ->
                add(constructor)
            }
        }
    }

    abstract fun getConstructor(value: T): AbstractTlbConstructor<*>

    fun storeTlbConstructor(cellBuilder: CellBuilder, value: T): C {
        val constructor = getConstructor(value)
        cellBuilder.storeBits(constructor.id)
        @Suppress("UNCHECKED_CAST")
        return constructor as C
    }

    fun loadTlbConstructor(cellSlice: CellSlice): C {
        val constructor = if (EXPERIMENTAL_BINARY_TREE_SEARCH) {
            val id = cellSlice.preloadBits(cellSlice.bits.size - cellSlice.bitsPosition)
            tlbConstructorTree[id] ?: throw UnknownTlbConstructorException(id)
        } else {
            var currentId = BitString.empty()
            sortedConstructors.firstOrNull { constructor ->
                if (constructor.id.size > currentId.size) {
                    currentId = cellSlice.preloadBits(constructor.id.size)
                }
                currentId == constructor.id
            } ?: throw UnknownTlbConstructorException()
        }
        cellSlice.skipBits(constructor.id.size)
        @Suppress("UNCHECKED_CAST")
        return constructor as C
    }

}
