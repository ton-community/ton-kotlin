@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("@type")
sealed class HashMapE<T : Any>

@Serializable
@SerialName("hme_empty")
class EmptyHashMapE<T : Any> : HashMapE<T>() {
    override fun toString(): String = "hme_empty"
}

@Serializable
@SerialName("hme_root")
data class RootHashMapE<T : Any>(
    val root: HashMapEdge<T>
) : HashMapE<T>() {
    override fun toString(): String = "hme_root(root=$root)"
}
