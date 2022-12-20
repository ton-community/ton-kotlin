@file:Suppress("NOTHING_TO_INLINE")

package org.ton.adnl.node

import org.ton.bitstring.BitString
import kotlin.jvm.JvmInline

@JvmInline
public value class AdnlCategoryMask(
    public val value: BitString = BitString(256)
) {
    init {
        require(value.size == 256)
    }

    public inline operator fun get(index: Int): Boolean =
        value[index]

    public inline infix fun xor(other: AdnlCategoryMask): AdnlCategoryMask =
        AdnlCategoryMask(value xor other.value)

    public inline infix fun or(other: AdnlCategoryMask): AdnlCategoryMask =
        AdnlCategoryMask(value or other.value)
}
