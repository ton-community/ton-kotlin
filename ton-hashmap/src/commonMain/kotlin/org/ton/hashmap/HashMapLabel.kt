@file:Suppress("OPT_IN_USAGE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString

@Serializable
@JsonClassDiscriminator("@type")
sealed interface HashMapLabel {
    val s: BitString

    companion object {
        @JvmStatic
        fun of(key: BitString, max: Int = key.size): HashMapLabel = HashMapLabel(key, max)
    }
}

fun HashMapLabel(key: BitString, max: Int = key.size): HashMapLabel {
    val len = 16 - max.toShort().countLeadingZeroBits()
    val longLength = 2 + len + key.size
    val shortLength = 1 + 2 * key.size + 1
    val sameLength = 2 + 1 + len
    val sameLabel = HashMapLabelSame.of(key)
    if (sameLabel != null) {
        val longOrShortLength = minOf(longLength, shortLength)
        if (sameLength < longOrShortLength) {
            return sameLabel
        }
    }
    if (shortLength <= longLength) {
        return HashMapLabelShort(key)
    }
    return HashMapLabelLong(key)
}
