package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hme_empty")
class EmptyHashMapE<out T> : HashMapE<T> {
    override fun nodes(): Sequence<Pair<BitString, Nothing>> = emptySequence()

    override fun toString(): String = "hme_empty"
}
