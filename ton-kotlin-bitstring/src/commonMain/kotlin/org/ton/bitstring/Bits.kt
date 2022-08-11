package org.ton.bitstring

fun Int.toBits(minSize: Int = 0): BooleanArray = toString(2).padStart(minSize, '0').map { it != '0' }.toBooleanArray()

/**
 * Augment bits with 1 and leading 0 to be divisible by 8 or 4 without remainder.
 * Mostly used for BOC serialization or Cell hash calculations.
 *
 * @param divider divider (`4` or `8`) after division by which there will be no remainder
 */
fun BooleanArray.augment(divider: Int = 8): BooleanArray {
    require(divider == 4 || divider == 8) { "expected divider: 4 or 8, actual: $divider" }
    val amount = divider - (size % 8)
    val overage = BooleanArray(amount) { index -> index == 0 }
    if (overage.isNotEmpty() && overage.size != divider) {
        return this + overage
    }
    return this
}

/**
 * Remove augmented bits.
 * Mostly used for BOC serialization or Cell hash calculations.
 */
fun BooleanArray.rollback(): BooleanArray {
    val index = sliceArray(0..size - 7).reversed().indexOf(true)
    check(index != -1) { "Incorrectly augment bits" }
    return sliceArray(0..-(index + 1))
}