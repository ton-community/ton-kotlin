@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("@type")
sealed class HashMapNode<T>

@Serializable
@SerialName("hmn_leaf")
data class HashMapNodeLeaf<T>(
    val value: T
) : HashMapNode<T>() {
    override fun toString() = "hmn_leaf(value=$value)"
}

@Serializable
@SerialName("hmn_fork")
class HashMapNodeFork<T>(
    val left: HashMapEdge<T>,
    val right: HashMapEdge<T>
) : HashMapNode<T>() {
    override fun toString() = "hmn_fork(left=$left, right=$right)"
}