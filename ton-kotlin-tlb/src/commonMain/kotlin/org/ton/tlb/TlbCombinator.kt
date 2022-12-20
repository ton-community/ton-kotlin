package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.UnknownTlbConstructorException
import org.ton.tlb.providers.TlbCombinatorProvider
import kotlin.reflect.KClass

abstract class TlbCombinator<T : Any>(
    override val baseClass: KClass<T>,
    vararg subClasses: Pair<KClass<out T>, TlbCodec<out T>>
) : AbstractTlbCombinator<T>(), TlbCombinatorProvider<T> {
    private val class2codec: MutableMap<KClass<out T>, TlbCodec<out T>>
    private val constructorTree = TlbConstructorTree<TlbCodec<out T>>()

    init {
        class2codec = subClasses.toMap().toMutableMap()
        subClasses.forEach { (_, constructor) ->
            if (constructor is TlbConstructor<out T>) {
                constructorTree.add(constructor.id, constructor)
            }
            if (constructor is TlbCombinator<out T>) {
                constructor.constructorTree.values().forEach { (key,value) ->
                    constructorTree.add(key, value)
                }
                class2codec.putAll(constructor.class2codec)
            }
        }
    }

    override fun tlbCombinator(): TlbCombinator<T> = this

    override fun loadTlb(cellSlice: CellSlice): T {
        val constructor = findTlbLoaderOrNull(cellSlice) ?: throw UnknownTlbConstructorException()
        if (constructor is TlbConstructor<*>) {
            cellSlice.skipBits(constructor.id.size)
        }
        return constructor.loadTlb(cellSlice)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        val constructor = findTlbStorerOrNull(value) ?: throw UnknownTlbConstructorException()
        if (constructor is TlbConstructor<*>) {
            cellBuilder.storeBits(constructor.id)
        }
        return constructor.storeTlb(cellBuilder, value)
    }

    protected open fun findTlbLoaderOrNull(cellSlice: CellSlice): TlbLoader<out T>? {
        val preloadBits = cellSlice.preloadBits(cellSlice.remainingBits)
        return findTlbLoaderOrNull(preloadBits)
    }

    protected open fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out T>? {
        val (_, constructor) = constructorTree.find(bitString)
            ?: return null
        return constructor
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun findTlbStorerOrNull(value: T): TlbStorer<T>? {
//        println("value: ${value::class} - ${class2codec.keys}")
        val constructor = class2codec[value::class]
            ?: return null
        return constructor as TlbStorer<T>
    }
}
