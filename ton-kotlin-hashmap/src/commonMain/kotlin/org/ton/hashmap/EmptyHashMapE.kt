package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString

@Serializable
@SerialName("hme_empty")
object EmptyHashMapE : HashMapE<Nothing> {
    override fun nodes(): Sequence<Pair<BitString, Nothing>> = emptySequence()

    override fun toString(): String = "hme_empty"
}

fun <T> EmptyHashMapE(): HashMapE<T> = EmptyHashMapE as HashMapE<T>
