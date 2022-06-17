package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hme_root")
data class RootHashMapE<T : Any>(
    val root: HashMapEdge<T>
) : HashMapE<T> {
    override fun <K> toMap(keyTransform: (BitString) -> K): Map<K, T> = toMap().mapKeys { (key, _) ->
        keyTransform(key)
    }

    override fun toMap(): Map<BitString, T> = root.toMap()

    override fun toString(): String = "hme_root(root=$root)"
}
