package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hmn_fork")
public data class HashMapNodeFork<out T>(
    val left: HashMapEdge<T>,
    val right: HashMapEdge<T>
) : HashMapNode<T> {
    override fun toString(): String = "(hmn_fork\nleft:$left right:$right)"
}
