package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hme_empty")
class EmptyHashMapE<T> : HashMapE<T> {
    override fun <K> toMap(keyTransform: (BitString) -> K): Map<K, T> = emptyMap()
    override fun toMap(): Map<BitString, T> = emptyMap()
    override fun toString(): String = "hme_empty"
}
