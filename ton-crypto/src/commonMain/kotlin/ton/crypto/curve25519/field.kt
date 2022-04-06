package ton.crypto.curve25519

import kotlin.experimental.and

interface FieldElement {
    /**
     * Determine if this FieldElement is negative, in the sense used in the ed25519 paper:
     * x is negative if the low bit is set.
     */
    val isNegative: Boolean get() = (toBytes()[0] and 1) == 1.toByte()

    /**
     * Determine if this FieldElement is zero.
     */
    val isZero: Boolean get() = toBytes().contentEquals(ByteArray(32))

    operator fun unaryMinus(): FieldElement
    operator fun plus(rhs: FieldElement): FieldElement
    operator fun minus(rhs: FieldElement): FieldElement
    operator fun times(rhs: FieldElement): FieldElement

    /**
     * Compute (self^(2^250-1), self^11), used as a helper function within invert() and pow22523().
     */
    fun pow25501(): Pair<FieldElement, FieldElement> {
        // Instead of managing which temporary variables are used
        // for what, we define as many as we need and leave stack
        // allocation to the compiler
        //
        // Each temporary variable t_i is of the form (self)^e_i.
        // Squaring t_i corresponds to multiplying e_i by 2,
        // so the pow2k function shifts e_i left by k places.
        // Multiplying t_i and t_j corresponds to adding e_i + e_j.
        //
        // Temporary t_i                      Nonzero bits of e_i
        //
        val t0 = square()                // 1         e_0 = 2^1
        val t1 = t0.square().square()    // 3         e_1 = 2^3
        val t2 = this * t1               // 3,0       e_2 = 2^3 + 2^0
        val t3 = t0 * t2                 // 3,1,0
        val t4 = t3.square()             // 4,2,1
        val t5 = t2 * t4                 // 4,3,2,1,0
        val t6 = t5.pow2k(5)          // 9,8,7,6,5
        val t7 = t6 * t5                 // 9,8,7,6,5,4,3,2,1,0
        val t8 = t7.pow2k(10)         // 19..10
        val t9 = t8 * t7                 // 19..0
        val t10 = t9.pow2k(20)        // 39..20
        val t11 = t10 * t9               // 39..0
        val t12 = t11.pow2k(10)       // 49..10
        val t13 = t12 * t7               // 49..0
        val t14 = t13.pow2k(50)       // 99..50
        val t15 = t14 * t13              // 99..0
        val t16 = t15.pow2k(100)      // 199..100
        val t17 = t16 * t15              // 199..0
        val t18 = t17.pow2k(50)       // 249..50
        val t19 = t18 * t13              // 249..0

        return t19 to t3
    }

    /**
     * Given a nonzero field element, compute its inverse.
     *
     * The inverse is computed as self^(p-2), since x^(p-2)x = x^(p-1) = 1 (mod p).
     *
     * This function returns zero on input zero.
     */
    fun invert(): FieldElement = let {
        // The bits of p-2 = 2^255 -19 -2 are 11010111111...11.
        // nonzero bits of exponent

        // t19: 249..0 ; t3: 3,1,0
        val (t19, t3) = pow25501()
        // t19: 249..0 ; t3: 3,1,0
        val t20 = t19.pow2k(5)
        // 254..5,3,1,0
        val t21 = t20 * t3

        t21
    }

    /**
     * Raise this field element to the power (p-5)/8 = 2^252 -3.
     */
    fun powP58(): FieldElement = let { self ->
        // The bits of (p-5)/8 are 101111.....11.
        // nonzero bits of exponent

        // 249..0
        val (t19, _) = pow25501()
        // 251..2
        val t20 = t19.pow2k(2)
        // 251..2,0
        val t21 = self * t20

        t21
    }


    /**
     * Attempt to compute `sqrt(1/self)` in constant time.
     *
     * Convenience wrapper around `sqrt_ratio_i`.
     *
     * This function always returns the nonnegative square root.
     *
     * @return
     * - `(true, +sqrt(1/self))` if `this` is a nonzero square;
     * - `(false, zero)` if `this` is zero;
     * - `(false, +sqrt(i/self))` if `this` is a nonzero nonsquare;
     */
    fun invsqrt(): Pair<Boolean, FieldElement> = sqrtRatioI(one(), this)

    /**
     * Compute self^2.
     */
    fun square(): FieldElement

    /**
     * Compute 2*self^2.
     */
    fun square2(): FieldElement

    /**
     * Given `k > 0`, return `self^(2^k)`.
     */
    fun pow2k(k: Int): FieldElement

    /**
     * Serialize this [FieldElement] to a 32-byte array. The encoding is canonical.
     */
    fun toBytes(): ByteArray

    companion object : FieldElementFactory by BACKEND.FIELD_ELEMENT_FACTORY {
        /**
         * Given `FieldElements` `u` and `v`, compute either `sqrt(u/v)` or `sqrt(i*u/v)` in constant time.
         *
         * This function always returns the nonnegative square root.
         *
         * @return
         * - `(true, +sqrt(u/v))` if `v` is nonzero and `u/v` is square
         * - `(true, zero)` if `u` is zero
         * - `(false, zero)` if `v` is zero and `u` is nonzero
         * - `(false, +sqrt(i*u/v))`  if `u/v` is nonsquare (so `i*u/v` is square).
         */
        fun sqrtRatioI(u: FieldElement, v: FieldElement): Pair<Boolean, FieldElement> {
            // Using the same trick as in ed25519 decoding, we merge the
            // inversion, the square root, and the square test as follows.
            //
            // To compute sqrt(α), we can compute β = α^((p+3)/8).
            // Then β^2 = ±α, so multiplying β by sqrt(-1) if necessary
            // gives sqrt(α).
            //
            // To compute 1/sqrt(α), we observe that
            //    1/β = α^(p-1 - (p+3)/8) = α^((7p-11)/8)
            //                            = α^3 * (α^7)^((p-5)/8).
            //
            // We can therefore compute sqrt(u/v) = sqrt(u)/sqrt(v)
            // by first computing
            //    r = u^((p+3)/8) v^(p-1-(p+3)/8)
            //      = u u^((p-5)/8) v^3 (v^7)^((p-5)/8)
            //      = (uv^3) (uv^7)^((p-5)/8).
            //
            // If v is nonzero and u/v is square, then r^2 = ±u/v,
            //                                     so vr^2 = ±u.
            // If vr^2 =  u, then sqrt(u/v) = r.
            // If vr^2 = -u, then sqrt(u/v) = r*sqrt(-1).
            //
            // If v is zero, r is also zero.

            val v3 = v.square() * v
            val v7 = v3.square() * v
            var r = (u * v3) * (u * v7).powP58()
            val check = v * r.square()

            val i = CONSTANTS.SQRT_M1

            val correctSignSqrt = check == u
            val flippedSignSqrt = check == -u
            val flippedSignSqrtI = check == -u * i

            val rPrime = CONSTANTS.SQRT_M1 * r
            r = if (flippedSignSqrt or flippedSignSqrtI) rPrime else r

            // Choose the nonnegative square root.
            val rIsNegative = r.isNegative
            r = if (rIsNegative) -r else r

            val wasNonzeroSquare = correctSignSqrt or flippedSignSqrt
            return wasNonzeroSquare to r
        }

        /**
         * Given a slice of public FieldElements, replace each with its inverse.
         *
         * All input FieldElements **MUST** be nonzero.
         */
        fun batchInvert(inputs: Array<FieldElement>) {
            // Montgomery’s Trick and Fast Implementation of Masked AES
            // Genelle, Prouff and Quisquater
            // Section 3.2

            val scratch = Array(inputs.size) { one() }

            // Keep an accumulator of all of the previous products
            var acc = one()

            // Pass through the input vector, recording the previous
            // products in the scratch space

            repeat(inputs.size) { index ->
                scratch[index] = acc
                acc *= inputs[index]
            }

            // acc is nonzero iff all inputs are nonzero
            check(!acc.isZero)

            // Compute the inverse of all products
            acc = acc.invert()

            inputs.reverse()
            scratch.reverse()

            // Pass through the vector backwards to compute the inverses
            // in place
            repeat(inputs.size) { index ->
                val tmp = acc * inputs[index]
                inputs[index] = acc * scratch[index]
                acc = tmp
            }
        }
    }
}

internal interface FieldElementFactory {
    fun zero(): FieldElement
    fun one(): FieldElement
    fun minus_one(): FieldElement
    fun fromBytes(data: ByteArray): FieldElement
}
