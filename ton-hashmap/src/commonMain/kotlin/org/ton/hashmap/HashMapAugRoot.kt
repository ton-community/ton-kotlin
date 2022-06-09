package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface HashMapAugRoot<X : Any, Y : Any> : HashmapAugE<X, Y> {
    val root: HashmapAugEdge<X, Y>
    override val extra: Y

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> of(
            root: HashmapAugEdge<X, Y>, extra: Y
        ): HashMapAugRoot<X, Y> = HashMapAugRootData(root, extra)
    }
}

@SerialName("ahme_root")
@Serializable
data class HashMapAugRootData<X : Any, Y : Any>(
    override val root: HashmapAugEdge<X, Y>,
    override val extra: Y
) : HashMapAugRoot<X, Y>, HashMapAugEData<X, Y>