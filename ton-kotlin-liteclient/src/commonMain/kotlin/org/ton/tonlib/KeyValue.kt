package org.ton.tonlib

import org.ton.crypto.decodeHex
import org.ton.crypto.encodeHex

internal abstract class KeyValue : Iterable<ByteArray> {
    abstract operator fun get(key: ByteArray): ByteArray?
    abstract operator fun set(key: ByteArray, value: ByteArray)
    abstract fun add(key: ByteArray, value: ByteArray): Boolean
}

internal class KeyValueInMemory : KeyValue() {
    private val map = mutableMapOf<String, ByteArray>()

    override fun get(key: ByteArray): ByteArray? {
        return map[key.encodeHex()]?.copyOf()
    }

    override fun set(key: ByteArray, value: ByteArray) {
        map[key.encodeHex()] = value.copyOf()
    }

    override fun add(key: ByteArray, value: ByteArray): Boolean {
        val stringKey = key.encodeHex()
        return if (map.containsKey(stringKey)) {
            false
        } else {
            map[stringKey] = value
            true
        }
    }

    override fun iterator(): Iterator<ByteArray> = iterator {
        for (value in map.keys) {
            yield(value.decodeHex())
        }
    }
}
