@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.TlbNegatedCodec
import org.ton.tlb.TlbNegatedCombinator
import org.ton.tlb.TlbObject
import kotlin.jvm.JvmStatic

public inline fun HashMapLabel(key: BitString, max: Int = key.size): HashMapLabel = HashMapLabel.of(key, max)

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface HashMapLabel : TlbObject {
    public fun toBitString(): BitString

    public companion object {
        @JvmStatic
        public fun of(key: BitString, max: Int = key.size): HashMapLabel {
            val len = 16 - max.toShort().countLeadingZeroBits()
            val longLength = 2 + len + key.size
            val shortLength = 1 + 2 * key.size + 1
            val sameLength = 2 + 1 + len
            val sameLabel = HmlSame.of(key)
            if (sameLabel != null) {
                val longOrShortLength = minOf(longLength, shortLength)
                if (sameLength < longOrShortLength) {
                    return sameLabel
                }
            }
            if (shortLength <= longLength) {
                return HmlShort(key)
            }
            return HmlLong(key)
        }

        @JvmStatic
        public fun tlbCodec(m: Int): TlbNegatedCodec<HashMapLabel> = HashMapLabelTlbCombinator(m)
    }
}

private class HashMapLabelTlbCombinator(
    m: Int,
) : TlbNegatedCombinator<HashMapLabel>(
    HashMapLabel::class,
    HmlLong::class to HmlLong.tlbCodec(m),
    HmlShort::class to HmlShort.tlbCodec(),
    HmlSame::class to HmlSame.tlbCodec(m),
)
