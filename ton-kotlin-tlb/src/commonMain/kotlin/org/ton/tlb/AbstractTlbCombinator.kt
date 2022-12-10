package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import kotlin.reflect.KClass

abstract class AbstractTlbCombinator<T : Any> : TlbCodec<T> {
    abstract val baseClass: KClass<T>

//    abstract val constructors: List<AbstractTlbConstructor<*>>
//
//    private val sortedConstructors by lazy {
//        constructors.sortedBy { it.id.size }
//    }
//
//    private val tlbConstructorTree by lazy {
//        TlbConstructorTree().apply {
//            constructors.sortedBy { it.id }.forEach { constructor ->
//                add(constructor)
//            }
//        }
//    }

//    abstract fun getConstructor(value: T): AbstractTlbConstructor<*>
//
//    fun storeTlbConstructor(cellBuilder: CellBuilder, value: T): C {
//        val constructor = getConstructor(value)
//        cellBuilder.storeBits(constructor.id)
//        @Suppress("UNCHECKED_CAST")
//        return constructor as C
//    }
//
//    fun loadTlbConstructor(cellSlice: CellSlice): C {
//        val constructor = tlbConstructorTree[id] ?: throw UnknownTlbConstructorException(id)
//        cellSlice.skipBits(constructor.id.size)
//        @Suppress("UNCHECKED_CAST")
//        return constructor as C
//    }
//
//    fun findPolymorphicSerializer
}
