@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString

@Serializable
@JsonClassDiscriminator("@type")
sealed interface HashMapE<T : Any> {
    fun <K> toMap(keyTransform: (BitString) -> K): Map<K, T>
    fun toMap(): Map<BitString, T>
}



