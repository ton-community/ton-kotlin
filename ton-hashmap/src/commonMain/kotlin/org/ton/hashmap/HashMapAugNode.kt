@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

interface HashMapAugNode<X : Any, Y : Any> {
    val extra: Y
}

@JsonClassDiscriminator("@type")
@Serializable
sealed interface HashMapAugNodeData<X : Any, Y : Any> : HashMapAugNode<X, Y>




