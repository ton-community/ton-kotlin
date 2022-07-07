package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hme_empty")
class EmptyHashMapE<T> : HashMapE<T> {
    override fun nodes(): Sequence<Pair<BitString, T>> = emptySequence()

    override fun toString(): String = "hme_empty"
}
