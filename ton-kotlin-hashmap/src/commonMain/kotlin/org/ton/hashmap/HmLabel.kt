@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.hashmap

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.*
import kotlin.jvm.JvmStatic

public inline fun HashMapLabel(key: BitString, max: Int = key.size): HmLabel = HmLabel.of(key, max)

@Serializable
@JsonClassDiscriminator("@type")
public sealed interface HmLabel : TlbObject {
    public fun toBitString(): BitString

    public companion object {
        @JvmStatic
        public fun of(key: BitString, max: Int = key.size): HmLabel {
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
        public fun tlbCodec(m: Int): TlbNegatedCodec<HmLabel> = HashMapLabelTlbCombinator(m)
    }
}

private class HashMapLabelTlbCombinator(
    m: Int,
    val hmlLong: TlbNegatedConstructor<HmlLong> =HmlLong.tlbCodec(m),
    val hmlShort: TlbNegatedConstructor<HmlShort> = HmlShort.tlbCodec(),
    val hmlSame: TlbNegatedConstructor<HmlSame> = HmlSame.tlbCodec(m)
) : TlbNegatedCombinator<HmLabel>(
    HmLabel::class,
    HmlLong::class to hmlLong,
    HmlShort::class to hmlShort,
    HmlSame::class to hmlSame,
) {
    override fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out HmLabel>? {
        return if (bitString[0]) {
            if (bitString[1]) {
                hmlSame
            } else {
                hmlLong
            }
        } else {
            hmlShort
        }
    }
}
