package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface HashMapAugNodeFork<X : Any, Y : Any> : HashMapAugNode<X, Y> {
    val left: HashmapAugEdge<X, Y>
    val right: HashmapAugEdge<X, Y>
    override val extra: Y

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> of(
            left: HashmapAugEdge<X, Y>,
            right: HashmapAugEdge<X, Y>,
            extra: Y
        ): HashMapAugNodeFork<X, Y> = HashMapAugNodeForkData(left, right, extra)
    }
}

@SerialName("ahmn_fork")
@Serializable
data class HashMapAugNodeForkData<X : Any, Y : Any>(
    override val left: HashmapAugEdge<X, Y>,
    override val right: HashmapAugEdge<X, Y>,
    override val extra: Y,
) : HashMapAugNodeFork<X, Y>, HashMapAugNodeData<X, Y>