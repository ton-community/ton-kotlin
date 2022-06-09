package org.ton.hashmap

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface HashmapAugEmpty<X : Any, Y : Any> : HashmapAugE<X, Y> {
    override val extra: Y

    companion object {
        @JvmStatic
        fun <X : Any, Y : Any> of(
            extra: Y
        ): HashmapAugEmpty<X, Y> = HashMapAugEmptyData(extra)
    }
}

@SerialName("ahme_empty")
@Serializable
data class HashMapAugEmptyData<X : Any, Y : Any>(
    override val extra: Y
) : HashmapAugEmpty<X, Y>, HashMapAugEData<X, Y>