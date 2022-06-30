package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hmn_fork")
class HashMapNodeFork<T>(
    val left: HashMapEdge<T>,
    val right: HashMapEdge<T>
) : HashMapNode<T> {
    override fun toString() = "hmn_fork(left:$left right:$right)"
}
