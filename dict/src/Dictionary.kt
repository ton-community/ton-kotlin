package org.ton.kotlin.dict

import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.serialization.CellSerializer

public open class Dictionary<K, V>(
    public val dict: RawDictionary,
    private val keySerializer: (K) -> BitString,
    private val keyDeserializer: (BitString) -> K,
    private val valueSerializer: CellSerializer<V>,
) : Map<K, V> {
    public constructor(
        cell: Cell,
        keySize: Int,
        keySerializer: (K) -> BitString,
        keyDeserializer: (BitString) -> K,
        valueSerializer: CellSerializer<V>
    ) : this(
        RawDictionary(cell, keySize),
        keySerializer,
        keyDeserializer,
        valueSerializer
    )

    override val size: Int
        get() = dict.asSequence().count()

    override val keys: Set<K>
        get() = loadKeys(CellContext.EMPTY).toSet()

    override val values: Collection<V>
        get() = loadValues(CellContext.EMPTY).toList()

    override val entries: Set<Map.Entry<K, V>>
        get() = loadEntries(CellContext.EMPTY).toSet()

    public fun loadEntries(context: CellContext): Sequence<Map.Entry<K, V>> =
        dict.iterator(context).asSequence().map { (key, value) ->
            val value = valueSerializer.load(value, context)
            val key = keyDeserializer(key)
            DictEntry(key, value)
        }

    public fun loadKeys(context: CellContext): Sequence<K> = dict.iterator(context).asSequence().map { (key, _) ->
        keyDeserializer(key)
    }

    public fun loadValues(context: CellContext): Sequence<V> = dict.iterator(context).asSequence().map { (_, value) ->
        valueSerializer.load(value, context)
    }

    override fun isEmpty(): Boolean {
        return dict.isEmpty()
    }

    override fun containsKey(key: K): Boolean {
        return dict[keySerializer(key)] != null
    }

    override fun containsValue(value: V): Boolean {
        return loadValues(CellContext.EMPTY).contains(value)
    }

    override fun get(key: K): V? = get(key, CellContext.EMPTY)

    public fun get(key: K, context: CellContext = CellContext.EMPTY): V? {
        return valueSerializer.load(dict.get(keySerializer(key), context) ?: return null, context)
    }

    public fun toMap(context: CellContext = CellContext.EMPTY): Map<K, V> {
        if (dict.isEmpty()) return emptyMap()
        val map = LinkedHashMap<K, V>()
        dict.iterator(context).forEach { (key, value) ->
            val value = valueSerializer.load(value, context)
            val key = keyDeserializer(key)
            map[key] = value
        }
        return map
    }

    private inner class DictEntry(
        override val key: K,
        override val value: V,
    ) : Map.Entry<K, V>
}
