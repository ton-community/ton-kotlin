@file:Suppress("NOTHING_TO_INLINE")

package ton.crypto.curve25519.u32

import ton.crypto.curve25519.FieldElement
import ton.crypto.curve25519.FieldElementFactory
import kotlin.experimental.and

class FieldElement2625(
    val value: IntArray = IntArray(10)
) : FieldElement {
    override fun unaryMinus(): FieldElement {
        return reduce(
            longArrayOf(
                ((0x3ffffed shl 4) - value[0]).toLong(),
                ((0x1ffffff shl 4) - value[1]).toLong(),
                ((0x3ffffff shl 4) - value[2]).toLong(),
                ((0x1ffffff shl 4) - value[3]).toLong(),
                ((0x3ffffff shl 4) - value[4]).toLong(),
                ((0x1ffffff shl 4) - value[5]).toLong(),
                ((0x3ffffff shl 4) - value[6]).toLong(),
                ((0x1ffffff shl 4) - value[7]).toLong(),
                ((0x3ffffff shl 4) - value[8]).toLong(),
                ((0x1ffffff shl 4) - value[9]).toLong(),
            )
        )
    }

    override fun plus(rhs: FieldElement): FieldElement2625 {
        rhs as FieldElement2625
        return FieldElement2625(IntArray(10) { i ->
            value[i] + rhs.value[i]
        })
    }

    override fun minus(rhs: FieldElement): FieldElement2625 {
        rhs as FieldElement2625
        // Compute a - b as ((a + 2^4 * p) - b) to avoid underflow.
        val b = rhs.value
        return reduce(
            longArrayOf(
                ((value[0] + (0x3ffffed shl 4)) - b[0]).toLong(),
                ((value[1] + (0x1ffffff shl 4)) - b[1]).toLong(),
                ((value[2] + (0x3ffffff shl 4)) - b[2]).toLong(),
                ((value[3] + (0x1ffffff shl 4)) - b[3]).toLong(),
                ((value[4] + (0x3ffffff shl 4)) - b[4]).toLong(),
                ((value[5] + (0x1ffffff shl 4)) - b[5]).toLong(),
                ((value[6] + (0x3ffffff shl 4)) - b[6]).toLong(),
                ((value[7] + (0x1ffffff shl 4)) - b[7]).toLong(),
                ((value[8] + (0x3ffffff shl 4)) - b[8]).toLong(),
                ((value[9] + (0x1ffffff shl 4)) - b[9]).toLong(),
            )
        )
    }

    override fun times(rhs: FieldElement): FieldElement2625 {
        // Alias self, rhs for more readable formulas
        val x = value
        val y = (rhs as FieldElement2625).value

        // We assume that the input limbs x[i], y[i] are bounded by:
        //
        // x[i], y[i] < 2^(26 + b) if i even
        // x[i], y[i] < 2^(25 + b) if i odd
        //
        // where b is a (real) parameter representing the excess bits of
        // the limbs.  We track the bitsizes of all variables through
        // the computation and solve at the end for the allowable
        // headroom bitsize b (which determines how many additions we
        // can perform between reductions or multiplications).

        val y1_19 = 19 * y[1] // This fits in a u32
        val y2_19 = 19 * y[2] // iff 26 + b + lg(19) < 32
        val y3_19 = 19 * y[3] // if  b < 32 - 26 - 4.248 = 1.752
        val y4_19 = 19 * y[4]
        val y5_19 = 19 * y[5] // below, b<2.5: this is a bottleneck,
        val y6_19 = 19 * y[6] // could be avoided by promoting to
        val y7_19 = 19 * y[7] // u64 here instead of in m()
        val y8_19 = 19 * y[8]
        val y9_19 = 19 * y[9]

        // What happens when we multiply x[i] with y[j] and place the
        // result into the (i+j)-th limb?
        //
        // x[i]      represents the value x[i]*2^ceil(i*51/2)
        // y[j]      represents the value y[j]*2^ceil(j*51/2)
        // z[i+j]    represents the value z[i+j]*2^ceil((i+j)*51/2)
        // x[i]*y[j] represents the value x[i]*y[i]*2^(ceil(i*51/2)+ceil(j*51/2))
        //
        // Since the radix is already accounted for, the result placed
        // into the (i+j)-th limb should be
        //
        // x[i]*y[i]*2^(ceil(i*51/2)+ceil(j*51/2) - ceil((i+j)*51/2)).
        //
        // The value of ceil(i*51/2)+ceil(j*51/2) - ceil((i+j)*51/2) is
        // 1 when both i and j are odd, and 0 otherwise.  So we add
        //
        //   x[i]*y[j] if either i or j is even
        // 2*x[i]*y[j] if i and j are both odd
        //
        // by using precomputed multiples of x[i] for odd i:
        val x1_2 = 2 * x[1] // This fits in a u32 iff 25 + b + 1 < 32
        val x3_2 = 2 * x[3] //                    iff b < 6
        val x5_2 = 2 * x[5]
        val x7_2 = 2 * x[7]
        val x9_2 = 2 * x[9]

        val z0 = m(x[0], y[0]) + m(x1_2, y9_19) + m(x[2], y8_19) + m(x3_2, y7_19) + m(x[4], y6_19) + m(x5_2, y5_19) + m(
            x[6],
            y4_19
        ) + m(x7_2, y3_19) + m(x[8], y2_19) + m(x9_2, y1_19)
        val z1 = m(x[0], y[1]) + m(x[1], y[0]) + m(x[2], y9_19) + m(x[3], y8_19) + m(x[4], y7_19) + m(x[5], y6_19) + m(
            x[6],
            y5_19
        ) + m(x[7], y4_19) + m(x[8], y3_19) + m(x[9], y2_19)
        val z2 = m(x[0], y[2]) + m(x1_2, y[1]) + m(x[2], y[0]) + m(x3_2, y9_19) + m(x[4], y8_19) + m(x5_2, y7_19) + m(
            x[6],
            y6_19
        ) + m(x7_2, y5_19) + m(x[8], y4_19) + m(x9_2, y3_19)
        val z3 = m(x[0], y[3]) + m(x[1], y[2]) + m(x[2], y[1]) + m(x[3], y[0]) + m(x[4], y9_19) + m(x[5], y8_19) + m(
            x[6],
            y7_19
        ) + m(x[7], y6_19) + m(x[8], y5_19) + m(x[9], y4_19)
        val z4 = m(x[0], y[4]) + m(x1_2, y[3]) + m(x[2], y[2]) + m(x3_2, y[1]) + m(x[4], y[0]) + m(x5_2, y9_19) + m(
            x[6],
            y8_19
        ) + m(x7_2, y7_19) + m(x[8], y6_19) + m(x9_2, y5_19)
        val z5 = m(x[0], y[5]) + m(x[1], y[4]) + m(x[2], y[3]) + m(x[3], y[2]) + m(x[4], y[1]) + m(x[5], y[0]) + m(
            x[6],
            y9_19
        ) + m(x[7], y8_19) + m(x[8], y7_19) + m(x[9], y6_19)
        val z6 = m(x[0], y[6]) + m(x1_2, y[5]) + m(x[2], y[4]) + m(x3_2, y[3]) + m(x[4], y[2]) + m(x5_2, y[1]) + m(
            x[6],
            y[0]
        ) + m(x7_2, y9_19) + m(x[8], y8_19) + m(x9_2, y7_19)
        val z7 = m(x[0], y[7]) + m(x[1], y[6]) + m(x[2], y[5]) + m(x[3], y[4]) + m(x[4], y[3]) + m(x[5], y[2]) + m(
            x[6],
            y[1]
        ) + m(x[7], y[0]) + m(x[8], y9_19) + m(x[9], y8_19)
        val z8 = m(x[0], y[8]) + m(x1_2, y[7]) + m(x[2], y[6]) + m(x3_2, y[5]) + m(x[4], y[4]) + m(x5_2, y[3]) + m(
            x[6],
            y[2]
        ) + m(x7_2, y[1]) + m(x[8], y[0]) + m(x9_2, y9_19)
        val z9 = m(x[0], y[9]) + m(x[1], y[8]) + m(x[2], y[7]) + m(x[3], y[6]) + m(x[4], y[5]) + m(x[5], y[4]) + m(
            x[6],
            y[3]
        ) + m(x[7], y[2]) + m(x[8], y[1]) + m(x[9], y[0])

        // How big is the contribution to z[i+j] from x[i], y[j]?
        //
        // Using the bounds above, we get:
        //
        // i even, j even:   x[i]*y[j] <   2^(26+b)*2^(26+b) = 2*2^(51+2*b)
        // i  odd, j even:   x[i]*y[j] <   2^(25+b)*2^(26+b) = 1*2^(51+2*b)
        // i even, j  odd:   x[i]*y[j] <   2^(26+b)*2^(25+b) = 1*2^(51+2*b)
        // i  odd, j  odd: 2*x[i]*y[j] < 2*2^(25+b)*2^(25+b) = 1*2^(51+2*b)
        //
        // We perform inline reduction mod p by replacing 2^255 by 19
        // (since 2^255 - 19 = 0 mod p).  This adds a factor of 19, so
        // we get the bounds (z0 is the biggest one, but calculated for
        // posterity here in case finer estimation is needed later):
        //
        //  z0 < ( 2 + 1*19 + 2*19 + 1*19 + 2*19 + 1*19 + 2*19 + 1*19 + 2*19 + 1*19 )*2^(51 + 2b) = 249*2^(51 + 2*b)
        //  z1 < ( 1 +  1   + 1*19 + 1*19 + 1*19 + 1*19 + 1*19 + 1*19 + 1*19 + 1*19 )*2^(51 + 2b) = 154*2^(51 + 2*b)
        //  z2 < ( 2 +  1   +  2   + 1*19 + 2*19 + 1*19 + 2*19 + 1*19 + 2*19 + 1*19 )*2^(51 + 2b) = 195*2^(51 + 2*b)
        //  z3 < ( 1 +  1   +  1   +  1   + 1*19 + 1*19 + 1*19 + 1*19 + 1*19 + 1*19 )*2^(51 + 2b) = 118*2^(51 + 2*b)
        //  z4 < ( 2 +  1   +  2   +  1   +  2   + 1*19 + 2*19 + 1*19 + 2*19 + 1*19 )*2^(51 + 2b) = 141*2^(51 + 2*b)
        //  z5 < ( 1 +  1   +  1   +  1   +  1   +  1   + 1*19 + 1*19 + 1*19 + 1*19 )*2^(51 + 2b) =  82*2^(51 + 2*b)
        //  z6 < ( 2 +  1   +  2   +  1   +  2   +  1   +  2   + 1*19 + 2*19 + 1*19 )*2^(51 + 2b) =  87*2^(51 + 2*b)
        //  z7 < ( 1 +  1   +  1   +  1   +  1   +  1   +  1   +  1   + 1*19 + 1*19 )*2^(51 + 2b) =  46*2^(51 + 2*b)
        //  z6 < ( 2 +  1   +  2   +  1   +  2   +  1   +  2   +  1   +  2   + 1*19 )*2^(51 + 2b) =  33*2^(51 + 2*b)
        //  z7 < ( 1 +  1   +  1   +  1   +  1   +  1   +  1   +  1   +  1   +  1   )*2^(51 + 2b) =  10*2^(51 + 2*b)
        //
        // So z[0] fits into a u64 if 51 + 2*b + lg(249) < 64
        //                         if b < 2.5.
        return reduce(longArrayOf(z0, z1, z2, z3, z4, z5, z6, z7, z8, z9))
    }

    override fun square() = reduce(squareInner())

    override fun square2(): FieldElement2625 {
        val coeffs = squareInner()
        for (i in value.indices) {
            coeffs[i] += coeffs[i]
        }
        return reduce(coeffs)
    }

    override fun pow2k(k: Int): FieldElement2625 {
        check(k > 0)
        var z = this
        repeat(k) {
            z = z.square()
        }
        return z
    }

    override fun toBytes(): ByteArray {
        val inp = value
        // Reduce the value represented by `in` to the range [0,2*p)
        val h = reduce(
            longArrayOf(
                inp[0].toLong(), inp[1].toLong(), inp[2].toLong(), inp[3].toLong(), inp[4].toLong(),
                inp[5].toLong(), inp[6].toLong(), inp[7].toLong(), inp[8].toLong(), inp[9].toLong(),
            )
        ).value

        // Let h be the value to encode.
        //
        // Write h = pq + r with 0 <= r < p.  We want to compute r = h mod p.
        //
        // Since h < 2*p, q = 0 or 1, with q = 0 when h < p and q = 1 when h >= p.
        //
        // Notice that h >= p <==> h + 19 >= p + 19 <==> h + 19 >= 2^255.
        // Therefore q can be computed as the carry bit of h + 19.

        var q = (h[0] + 19) shr 26
        q = h[1] + q shr 25
        q = h[2] + q shr 26
        q = h[3] + q shr 25
        q = h[4] + q shr 26
        q = h[5] + q shr 25
        q = h[6] + q shr 26
        q = h[7] + q shr 25
        q = h[8] + q shr 26
        q = h[9] + q shr 25

        check(q == 0 || q == 1)

        // Now we can compute r as r = h - pq = r - (2^255-19)q = r + 19q - 2^255q

        h[0] += 19 * q

        // Now carry the result to compute r + 19q...
        h[1] += h[0] shr 26
        h[0] = h[0] and LOW_26_BITS
        h[2] += h[1] shr 25
        h[1] = h[1] and LOW_25_BITS
        h[3] += h[2] shr 26
        h[2] = h[2] and LOW_26_BITS
        h[4] += h[3] shr 25
        h[3] = h[3] and LOW_25_BITS
        h[5] += h[4] shr 26
        h[4] = h[4] and LOW_26_BITS
        h[6] += h[5] shr 25
        h[5] = h[5] and LOW_25_BITS
        h[7] += h[6] shr 26
        h[6] = h[6] and LOW_26_BITS
        h[8] += h[7] shr 25
        h[7] = h[7] and LOW_25_BITS
        h[9] += h[8] shr 26
        h[8] = h[8] and LOW_26_BITS

        // ... but instead of carrying the value
        // (h[9] >> 25) = q*2^255 into another limb,
        // discard it, subtracting the value from h.
        check(h[9] shr 25 == 0 || h[9] shr 25 == 1)
        h[9] = h[9] and LOW_25_BITS

        val s = ByteArray(32)
        s[0] = (h[0] shr 0).toByte()
        s[1] = (h[0] shr 8).toByte()
        s[2] = (h[0] shr 16).toByte()
        s[3] = ((h[0] shr 24) or (h[1] shl 2)).toByte()
        s[4] = (h[1] shr 6).toByte()
        s[5] = (h[1] shr 14).toByte()
        s[6] = ((h[1] shr 22) or (h[2] shl 3)).toByte()
        s[7] = (h[2] shr 5).toByte()
        s[8] = (h[2] shr 13).toByte()
        s[9] = ((h[2] shr 21) or (h[3] shl 5)).toByte()
        s[10] = (h[3] shr 3).toByte()
        s[11] = (h[3] shr 11).toByte()
        s[12] = ((h[3] shr 19) or (h[4] shl 6)).toByte()
        s[13] = (h[4] shr 2).toByte()
        s[14] = (h[4] shr 10).toByte()
        s[15] = (h[4] shr 18).toByte()
        s[16] = (h[5] shr 0).toByte()
        s[17] = (h[5] shr 8).toByte()
        s[18] = (h[5] shr 16).toByte()
        s[19] = ((h[5] shr 24) or (h[6] shl 1)).toByte()
        s[20] = (h[6] shr 7).toByte()
        s[21] = (h[6] shr 15).toByte()
        s[22] = ((h[6] shr 23) or (h[7] shl 3)).toByte()
        s[23] = (h[7] shr 5).toByte()
        s[24] = (h[7] shr 13).toByte()
        s[25] = ((h[7] shr 21) or (h[8] shl 4)).toByte()
        s[26] = (h[8] shr 4).toByte()
        s[27] = (h[8] shr 12).toByte()
        s[28] = ((h[8] shr 20) or (h[9] shl 6)).toByte()
        s[29] = (h[9] shr 2).toByte()
        s[30] = (h[9] shr 10).toByte()
        s[31] = (h[9] shr 18).toByte()

        // Check that high bit is cleared
        check(s[31] and 0b10000000.toByte() == 0.toByte())
        return s
    }

    override fun toString(): String = "FieldElement2625(${value.joinToString()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FieldElement2625

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int = value.contentHashCode()

    object FactoryU32 : FieldElementFactory {
        override fun zero() = FieldElement2625(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        override fun one() = FieldElement2625(intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0))

        override fun minus_one() = FieldElement2625(
            intArrayOf(
                0x3ffffec, 0x1ffffff, 0x3ffffff, 0x1ffffff, 0x3ffffff, 0x1ffffff, 0x3ffffff, 0x1ffffff,
                0x3ffffff, 0x1ffffff
            )
        )

        override fun fromBytes(data: ByteArray): FieldElement {
            val h = LongArray(10)
            h[0] = data.load4(0)
            h[0] = data.load4(0)
            TODO()
        }
    }
}

const val LOW_25_BITS = (1 shl 25) - 1
const val LOW_26_BITS = (1 shl 26) - 1

private inline fun ByteArray.load3(offset: Int) = let { b ->
    (b[offset].toLong()) or (b[offset + 1].toLong() shl 8) or (b[offset + 2].toLong() shl 16)
}

private inline fun ByteArray.load4(offset: Int) = let { b ->
    (b[offset].toLong()) or (b[offset + 1].toLong() shl 8) or (b[offset + 2].toLong() shl 16) or (b[offset + 3].toLong() shl 16)
}

/**
 * Given unreduced coefficients `z[0], ..., z[9]` of any size,
 * carry and reduce them mod p to obtain a `FieldElement2625`
 * whose coefficients have excess `b < 0.007`.
 *
 * In other words, each coefficient of the result is bounded by
 * either `2^(25 + 0.007)` or `2^(26 + 0.007)`, as appropriate.
 */
private fun reduce(z: LongArray): FieldElement2625 {
    // Perform two halves of the carry chain in parallel.
    carry(z, 0); carry(z, 4)
    carry(z, 1); carry(z, 5)
    carry(z, 2); carry(z, 6)
    carry(z, 3); carry(z, 7)
    // Since z[3] < 2^64, c < 2^(64-25) = 2^39,
    // so    z[4] < 2^26 + 2^39 < 2^39.0002
    carry(z, 4); carry(z, 8)
    // Now z[4] < 2^26
    // and z[5] < 2^25 + 2^13.0002 < 2^25.0004 (good enough)

    // Last carry has a multiplication by 19:
    z[0] += 19 * (z[9] shr 25)
    z[9] = z[9] and LOW_25_BITS.toLong()

    // Since z[9] < 2^64, c < 2^(64-25) = 2^39,
    //    so z[0] + 19*c < 2^26 + 2^43.248 < 2^43.249.
    carry(z, 0)

    return FieldElement2625(
        intArrayOf(
            z[0].toInt(), z[1].toInt(), z[2].toInt(), z[3].toInt(), z[4].toInt(),
            z[5].toInt(), z[6].toInt(), z[7].toInt(), z[8].toInt(), z[9].toInt(),
        )
    )
}

/**
 * Carry the value from limb i = 0..8 to limb i+1
 */
private inline fun carry(z: LongArray, i: Int) {
    check(i < 9)
    if (i % 2 == 0) {
        // Even limbs have 26 bits
        z[i + 1] += z[i] shr 26
        z[i] = z[i] and LOW_26_BITS.toLong()
    } else {
        // Odd limbs have 25 bits
        z[i + 1] += z[i] shr 25
        z[i] = z[i] and LOW_25_BITS.toLong()
    }
}

/**
 * Helper function to multiply two 32-bit integers with 64 bits of output.
 */
private inline fun m(x: Int, y: Int) = x.toLong() * y.toLong()

private fun FieldElement2625.squareInner(): LongArray {
    // Optimized version of multiplication for the case of squaring.
    // Pre- and post- conditions identical to multiplication function.
    val x = value
    val x0_2 = 2 * x[0]
    val x1_2 = 2 * x[1]
    val x2_2 = 2 * x[2]
    val x3_2 = 2 * x[3]
    val x4_2 = 2 * x[4]
    val x5_2 = 2 * x[5]
    val x6_2 = 2 * x[6]
    val x7_2 = 2 * x[7]
    val x5_19 = 19 * x[5]
    val x6_19 = 19 * x[6]
    val x7_19 = 19 * x[7]
    val x8_19 = 19 * x[8]
    val x9_19 = 19 * x[9]

    // This block is rearranged so that instead of doing a 32-bit multiplication by 38, we do a
    // 64-bit multiplication by 2 on the results.  This is because lg(38) is too big: we would
    // have less than 1 bit of headroom left, which is too little.
    val z = LongArray(10)
    z[0] = m(x[0], x[0]) + m(x2_2, x8_19) + m(x4_2, x6_19) + (m(x1_2, x9_19) + m(x3_2, x7_19) + m(x[5], x5_19)) * 2
    z[1] = m(x0_2, x[1]) + m(x3_2, x8_19) + m(x5_2, x6_19) + (m(x[2], x9_19) + m(x[4], x7_19)) * 2
    z[2] = m(x0_2, x[2]) + m(x1_2, x[1]) + m(x4_2, x8_19) + m(x[6], x6_19) + (m(x3_2, x9_19) + m(x5_2, x7_19)) * 2
    z[3] = m(x0_2, x[3]) + m(x1_2, x[2]) + m(x5_2, x8_19) + (m(x[4], x9_19) + m(x[6], x7_19)) * 2
    z[4] = m(x0_2, x[4]) + m(x1_2, x3_2) + m(x[2], x[2]) + m(x6_2, x8_19) + (m(x5_2, x9_19) + m(x[7], x7_19)) * 2
    z[5] = m(x0_2, x[5]) + m(x1_2, x[4]) + m(x2_2, x[3]) + m(x7_2, x8_19) + m(x[6], x9_19) * 2
    z[6] = m(x0_2, x[6]) + m(x1_2, x5_2) + m(x2_2, x[4]) + m(x3_2, x[3]) + m(x[8], x8_19) + m(x7_2, x9_19) * 2
    z[7] = m(x0_2, x[7]) + m(x1_2, x[6]) + m(x2_2, x[5]) + m(x3_2, x[4]) + m(x[8], x9_19) * 2
    z[8] = m(x0_2, x[8]) + m(x1_2, x7_2) + m(x2_2, x[6]) + m(x3_2, x5_2) + m(x[4], x[4]) + m(x[9], x9_19) * 2
    z[9] = m(x0_2, x[9]) + m(x1_2, x[8]) + m(x2_2, x[7]) + m(x3_2, x[6]) + m(x4_2, x[5])

    return z
}