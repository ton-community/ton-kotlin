package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("hmn_leaf")
public data class HashMapNodeLeaf<out T>(
    val value: T
) : HashMapNode<T> {
    override fun toString(): String = "(hmn_leaf value:$value)"
}
