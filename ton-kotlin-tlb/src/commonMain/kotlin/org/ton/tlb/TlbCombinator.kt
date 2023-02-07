package org.ton.tlb

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.exception.UnknownTlbConstructorException
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.reflect.KClass

public abstract class TlbCombinator<T : Any>(
    override val baseClass: KClass<T>,
    vararg subClasses: Pair<KClass<out T>, TlbCodec<out T>>
) : AbstractTlbCombinator<T>(), TlbCombinatorProvider<T> {
    private val class2codec: MutableMap<KClass<out T>, TlbCodec<out T>>
    private val codec2Class = HashMap<BitString, TlbCodec<out T>>()

    init {
        class2codec = subClasses.toMap().toMutableMap()

        subClasses.forEach { (_, constructor) ->
            when (constructor) {
                is TlbConstructor<out T> -> addConstructor(constructor)
                is TlbConstructorProvider<out T> -> addConstructor(constructor.tlbConstructor())
                is TlbCombinator<out T> -> addCombinator(constructor)
                is TlbCombinatorProvider<out T> -> addCombinator(constructor.tlbCombinator())
            }
        }
    }

    private fun addConstructor(constructor: TlbConstructor<out T>) {
        codec2Class.set(constructor.id, constructor)
    }

    private fun addCombinator(combinator: TlbCombinator<out T>) {
        codec2Class.putAll(combinator.codec2Class)
        class2codec.putAll(combinator.class2codec)
    }

    override fun tlbCombinator(): TlbCombinator<T> = this

    override fun loadTlb(cellSlice: CellSlice): T {
        val constructor = findTlbLoaderOrNull(cellSlice) ?: throw UnknownTlbConstructorException(
            cellSlice.preloadBits(32)
        )
        if (constructor is TlbConstructor<*>) {
            cellSlice.skipBits(constructor.id.size)
        }
        return constructor.loadTlb(cellSlice)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: T) {
        val storer = findTlbStorerOrNull(value) ?: throw UnknownTlbConstructorException()
        if (storer is TlbConstructorProvider<*>) {
            cellBuilder.storeBits(storer.tlbConstructor().id)
        } else if (storer is TlbConstructor<*>) {
            cellBuilder.storeBits(storer.id)
        }
        return storer.storeTlb(cellBuilder, value)
    }

    protected open fun findTlbLoaderOrNull(cellSlice: CellSlice): TlbLoader<out T>? {
        val preloadBits = cellSlice.preloadBits(cellSlice.remainingBits)
        return findTlbLoaderOrNull(preloadBits)
    }

    protected open fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out T>? {
        val result = codec2Class.maxByOrNull {
            println("key = ${it.key}, bitString = $bitString} - ${it.key.commonPrefixWith(bitString)} (${it.key.commonPrefixWith(bitString).size}) - ${it.value}")
            it.key.commonPrefixWith(bitString).size
        }?.value
        println("max by: $result")
        return result
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun findTlbStorerOrNull(value: T): TlbStorer<T>? {
        val constructor = class2codec[value::class]
            ?: return null
        return constructor as TlbStorer<T>
    }
}
