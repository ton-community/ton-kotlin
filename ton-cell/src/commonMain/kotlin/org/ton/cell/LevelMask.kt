package org.ton.cell

data class LevelMask(
    val mask: Int = 0
) {
    val level: Int get() = Int.SIZE_BITS - mask.countLeadingZeroBits()
    val hashIndex: Int get() = mask.countOneBits()
    val hashCount: Int get() = hashIndex + 1

    fun apply(level: Int): LevelMask {
        require(level < 32)
        return LevelMask(mask and ((1 shl level) - 1))
    }

    fun isSignificant(level: Int): Boolean {
        require(level < 32)
        val result = level == 0 || ((mask shr (level - 1)) % 2 != 0)
        assert(result == (apply(level).level == level))
        return result
    }

    infix fun or(other: LevelMask): LevelMask =
        LevelMask(mask or other.mask)

    infix fun shr(bitCount: Int): LevelMask =
        LevelMask(mask shr bitCount)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LevelMask) return false
        if (mask != other.mask) return false
        return true
    }

    override fun hashCode(): Int = mask

    companion object {
        private val ZERO = LevelMask(0)

        fun level(level: Int): LevelMask {
            require(level < 32)
            if (level == 0) return ZERO
            return LevelMask(1 shl (level - 1))
        }
    }
}
