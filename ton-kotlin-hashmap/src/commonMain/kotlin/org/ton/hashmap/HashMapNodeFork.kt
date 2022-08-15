package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hmn_fork")
data class HashMapNodeFork<T>(
    val left: HashMapEdge<T>,
    val right: HashMapEdge<T>
) : HashMapNode<T> {
    override fun toString() = "(hmn_fork\nleft:$left right:$right)"
}
