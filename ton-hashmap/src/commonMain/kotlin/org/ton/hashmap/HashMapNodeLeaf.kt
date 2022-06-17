package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hmn_leaf")
data class HashMapNodeLeaf<T : Any>(
    val value: T
) : HashMapNode<T> {
    override fun toString() = "hmn_leaf(value=$value)"
}
