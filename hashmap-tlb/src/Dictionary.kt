@file:Suppress("PackageDirectoryMismatch")

package org.ton.kotlin.dict

import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.tlb.TlbCodec

public open class Dictionary<K, V>(
    public val dict: RawDictionary,
    protected val keyCodec: DictionaryKeyCodec<K>,
    protected val valueCodec: TlbCodec<V>,
    protected val context: CellContext = CellContext.EMPTY
) : Map<K, V> {
    init {
        require(dict.keySize == keyCodec.keySize)
    }

    public constructor(
        cell: Cell?,
        keyCodec: DictionaryKeyCodec<K>,
        valueCodec: TlbCodec<V>,
        context: CellContext = CellContext.EMPTY
    ) : this(
        RawDictionary(cell, keyCodec.keySize),
        keyCodec,
        valueCodec,
        context
    )

    public constructor(
        map: Map<out K, V>,
        keyCodec: DictionaryKeyCodec<K>,
        valueCodec: TlbCodec<V>,
        context: CellContext = CellContext.EMPTY
    ) : this(RawDictionary(keyCodec.keySize).apply {
        val builder = CellBuilder()
        map.forEach { (key, value) ->
            builder.reset()
            valueCodec.storeTlb(builder, value)
            val slice = builder.endCell().beginParse()
            val bitString = keyCodec.encodeKey(key)
            set(bitString, slice, context)
        }
    }, keyCodec, valueCodec, context)

    public constructor(
        dictionary: Dictionary<K, V>,
        context: CellContext = CellContext.EMPTY
    ) : this(
        dictionary.dict.root,
        dictionary.keyCodec,
        dictionary.valueCodec,
        context
    )

    override val size: Int
        get() = dict.iterator(context).asSequence().count()

    override val keys: Set<K>
        get() = loadKeys().toSet()

    override val values: Collection<V>
        get() = loadValues().toList()

    override val entries: Set<Map.Entry<K, V>>
        get() = loadEntries().toSet()

    public fun loadEntries(context: CellContext = this.context): Sequence<Map.Entry<K, V>> =
        dict.iterator(context).asSequence().map { (key, value) ->
//            val value = valueSerializer.load(value, context) TODO: use context
            val value = valueCodec.loadTlb(value)
            val key = keyCodec.decodeKey(key)
            DictEntry(key, value)
        }

    public fun loadKeys(context: CellContext = this.context): Sequence<K> =
        dict.iterator(context).asSequence().map { (key, _) ->
            keyCodec.decodeKey(key)
        }

    public fun loadValues(context: CellContext = this.context): Sequence<V> =
        dict.iterator(context).asSequence().map { (_, value) ->
//        valueSerializer.load(value, context) TODO: use context
            valueCodec.loadTlb(value)
        }

    override fun isEmpty(): Boolean {
        return dict.isEmpty()
    }

    override fun containsKey(key: K): Boolean {
        return dict[keyCodec.encodeKey(key)] != null
    }

    override fun containsValue(value: V): Boolean {
        return loadValues().contains(value)
    }

    override fun get(key: K): V? = get(key, context)

    public fun get(key: K, context: CellContext = this.context): V? {
        // TODO: use context
//        return valueSerializer.loadTlb(dict.get(keySerializer(key), context) ?: return null, context)
        return valueCodec.loadTlb(dict.get(keyCodec.encodeKey(key), context) ?: return null)
    }

    public fun toMap(context: CellContext = this.context): Map<K, V> {
        if (dict.isEmpty()) return emptyMap()
        return toMap(LinkedHashMap<K, V>(), context)
    }

    public fun <M : MutableMap<in K, in V>> toMap(destination: M, context: CellContext = this.context): M {
        if (dict.isEmpty()) return destination
        dict.iterator(context).forEach { (key, value) ->
            val value = valueCodec.loadTlb(value)
            val key = keyCodec.decodeKey(key)
            destination[key] = value
        }
        return destination
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Dictionary<*, *>
        return dict == other.dict
    }

    override fun hashCode(): Int = dict.hashCode()

    override fun toString(): String = dict.toString()

    private inner class DictEntry(
        override val key: K,
        override val value: V,
    ) : Map.Entry<K, V>
}

public fun <K, V> Map<K, V>.toDictionary(
    keyCodec: DictionaryKeyCodec<K>,
    valueCodec: TlbCodec<V>,
    context: CellContext = CellContext.EMPTY
): Dictionary<K, V> = Dictionary(this, keyCodec, valueCodec, context)

public fun foo() {
    val dictionary: Dictionary<String, Int> = Any() as Dictionary<String, Int>
    dictionary.toMap()
}

//
//public open class MutableDictionary<K, V>(
//    dict: RawDictionary,
//    keySerializer: (K) -> BitString,
//    keyDeserializer: (BitString) -> K,
//    valueSerializer: TlbCodec<V>,
//) : Dictionary<K, V>(dict, keySerializer, keyDeserializer, valueSerializer), MutableMap<K, V> {
//    override val keys: MutableSet<K>
//        get() = TODO("Not yet implemented")
//    override val values: MutableCollection<V>
//        get() = TODO("Not yet implemented")
//    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
//        get() = object : MutableSet<MutableMap.MutableEntry<K, V>> {
//            val entries = loadEntries(CellContext.EMPTY)
//
//            override fun iterator(): MutableIterator<MutableMap.MutableEntry<K, V>> = entries.iterator()
//
//            override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun remove(element: MutableMap.MutableEntry<K, V>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun addAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun removeAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun retainAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun clear() {
//                TODO("Not yet implemented")
//            }
//
//            override val size: Int
//                get() = TODO("Not yet implemented")
//
//            override fun isEmpty(): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun contains(element: MutableMap.MutableEntry<K, V>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//            override fun containsAll(elements: Collection<MutableMap.MutableEntry<K, V>>): Boolean {
//                TODO("Not yet implemented")
//            }
//
//        }
//
//    override fun put(key: K, value: V): V? {
//        val serializedKey = keySerializer(key)
//        val serializedValue = CellBuilder().apply {
//            storeTlb(valueCodec, value)
//        }.endCell().beginParse()
//        return dict.set(serializedKey, serializedValue)?.run {
//            loadTlb(valueCodec)
//        }
//    }
//
//    override fun remove(key: K): V? {
//        val serializedKey = keySerializer(key)
//        return dict.remove(serializedKey)?.run {
//            loadTlb(valueCodec)
//        }
//    }
//
//    override fun putAll(from: Map<out K, V>) {
//        from.forEach { (key, value) ->
//            put(key, value)
//        }
//    }
//
//    override fun clear() {
//        dict.clear()
//    }
//
//    private class EntriesSet<K, V>(
//        set: Map.Entry<K, V>,
//    ) : MutableSet<MutableMap.MutableEntry>
//}
