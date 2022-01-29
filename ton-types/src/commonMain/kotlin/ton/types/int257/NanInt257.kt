package ton.types.int257

object NanInt257 : Int257 {
    override val sign: Int = 0
    override val isNan: Boolean = true
    override val isZero: Boolean = false
    override fun toString(): String = "NaN"
    override fun toString(radix: Int): String = toString()
    override fun equals(other: Any?): Boolean {
        if (other !is Int257) return false
        return other.isNan
    }

    override fun plus(other: Int257) = NanInt257
    override fun minus(other: Int257) = NanInt257
    override fun unaryMinus(): Int257 = NanInt257
    override fun times(other: Int257) = NanInt257
    override fun div(other: Int257) = NanInt257
    override fun mod(other: Int257) = NanInt257
    override fun divMod(other: Int257): Pair<NanInt257, NanInt257> = NanInt257 to NanInt257
    override fun compareTo(other: Int257): Int = throw ArithmeticException("NaN")
    override fun shl(other: Int) = NanInt257
    override fun shr(other: Int) = NanInt257
    override fun and(other: Int257) = NanInt257
    override fun or(other: Int257) = NanInt257
    override fun xor(other: Int257) = NanInt257
    override fun not() = NanInt257

    override fun toInt(): Int = throw ArithmeticException("NaN")
    override fun toLong(): Long = throw ArithmeticException("NaN")
}
