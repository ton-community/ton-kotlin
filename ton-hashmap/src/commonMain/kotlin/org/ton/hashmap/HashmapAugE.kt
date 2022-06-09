@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

interface HashmapAugE<X : Any, Y : Any> {
    val extra: Y
}

@JsonClassDiscriminator("@type")
@Serializable
sealed interface HashMapAugEData<X : Any, Y : Any> : HashmapAugE<X, Y>