@file:Suppress("NOTHING_TO_INLINE")

package org.ton.adnl.node

import org.ton.bitstring.BitString
import kotlin.jvm.JvmInline

@JvmInline
value class AdnlCategoryMask(
    val value: BitString = BitString(256)
) {
    init {
        require(value.size == 256)
    }

    inline operator fun get(index: Int) = value[index]
    inline infix fun xor(other: AdnlCategoryMask) = AdnlCategoryMask(value xor other.value)
    inline infix fun or(other: AdnlCategoryMask) = AdnlCategoryMask(value or other.value)
}
