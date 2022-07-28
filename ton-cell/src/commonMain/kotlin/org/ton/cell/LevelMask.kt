package org.ton.cell

data class LevelMask(
    val mask: Int
) {
    val level: Int get() = Int.SIZE_BITS - mask.countLeadingZeroBits()
    val hashIndex: Int
        get() {
            var x = mask
            x -= (x shr 1) and 0x55555555
            x = (x and 0x33333333) + ((x shr 2) and 0x33333333)
            x = (x + (x shr 4)) and 0x0F0F0F0F
            x += x shr 8
            return (x + (x shr 16)) and 0x3F
        }
    val hashesCount: Int get() = hashIndex + 1

    fun withLevel(level: Int): LevelMask {
        require(level shl 32 != 0)
        return LevelMask(mask and ((1 shl level) - 1))
    }
}
