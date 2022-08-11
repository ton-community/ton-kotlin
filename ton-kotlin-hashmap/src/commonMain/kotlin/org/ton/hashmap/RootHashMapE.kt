package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hme_root")
data class RootHashMapE<T>(
    val root: HashMapEdge<T>
) : HashMapE<T> {

    override fun nodes(): Sequence<Pair<BitString, T>> = root.nodes()

    override fun toString(): String = "(hme_root\nroot:$root)"
}
