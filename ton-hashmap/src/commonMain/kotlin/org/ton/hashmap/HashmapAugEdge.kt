package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface HashmapAugEdge<X : Any, Y : Any> {
    val label: HashMapLabel
    val node: HashMapAugNode<X, Y>

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> of(
            label: HashMapLabel,
            node: HashMapAugNode<X, Y>
        ): HashmapAugEdge<X, Y> = HashmapAugEdgeData(label, node)
    }
}

@SerialName("ahm_edge")
@Serializable
data class HashmapAugEdgeData<X : Any, Y : Any>(
    override val label: HashMapLabel,
    override val node: HashMapAugNode<X, Y>
) : HashmapAugEdge<X, Y>