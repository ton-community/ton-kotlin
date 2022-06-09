package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface HashMapAugNodeLeaf<X : Any, Y : Any> : HashMapAugNode<X, Y> {
    override val extra: Y
    val value: X
}

@SerialName("ahmn_leaf")
@Serializable
data class HashMapAugNodeLeafData<X : Any, Y : Any>(
    override val extra: Y,
    override val value: X
) : HashMapAugNodeLeaf<X, Y>, HashMapAugNodeData<X, Y>