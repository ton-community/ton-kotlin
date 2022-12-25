package org.ton.bigint

import org.ton.bigint.BigInt.Companion.LONG_MASK
import org.ton.bigint.BigInt.Companion.bitLengthForInt

internal class MutableBigInt {
    private var value: IntArray
    private var intLen: Int
    private var offset: Int = 0

    val magnitudeArray: IntArray
        get() {
            if (offset > 0 || value.size != intLen) {
                //             int[] tmp = Arrays.copyOfRange(value, offset, offset + intLen);
                val tmp = value.copyOfRange(offset, offset + intLen)
                value.fill(0)
                offset = 0
                intLen = tmp.size
                value = tmp
            }
            return value
        }

    val isZero get() = intLen == 0
    val isOne get() = intLen == 1 && value[offset] == 1
    val isEven get() = intLen == 0 || (value[offset + intLen - 1] and 1) == 0
    val isOdd get() = !isEven
    val isNormal: Boolean
        get() {
            if (intLen + offset > value.size) return false
            if (intLen == 0) return true
            return value[offset] != 0
        }

    constructor() {
        value = IntArray(1)
        intLen = 0
    }

    constructor(value: Int) {
        this.value = IntArray(1)
        this.value[0] = value
        intLen = 1
    }

    constructor(values: IntArray) {
        this.value = values
        intLen = values.size
    }

    constructor (value: MutableBigInt) {
        this.value = value.value.copyOf()
        intLen = value.intLen
    }

    private fun ones(n: Int) {
        if (n > value.size) {
            value = IntArray(n)
        }
        value.fill(-1)
        intLen = n
    }

    private fun toLong(): Long {
        check(intLen <= 2) { "this MutableBigInt exceeds the range of long" }
        if (intLen == 0) {
            return 0
        }
        val d = value[offset].toLong()
        return if (intLen == 2) (d shl 32) or (value[offset + 1].toLong() and LONG_MASK) else d
    }

    fun toBigInt(sign: Int): BigInt {
        if (intLen == 0 || sign == 0) return BigInt.ZERO
        return BigInt(magnitudeArray, 1)
    }

    fun toBigInt(): BigInt {
        normalize()
        return toBigInt(if (isZero) 0 else 1)
    }

    fun toCompactValue(sign: Int): Long {
        if (intLen == 0 || sign == 0) return 0L
        val mag = magnitudeArray
        val len = mag.size
        val d = mag[0]
        if (len > 2 || (d < 0 && len == 2)) return INFLATED
        val v = if (len == 2) {
            ((mag[1].toLong() and LONG_MASK) or ((d.toLong() and LONG_MASK) shl 32))
        } else {
            d.toLong() and LONG_MASK
        }
        return if (sign == -1) -v else v
    }

    fun clear() {
        reset()
        value.fill(0, 0, value.size)
    }

    fun reset() {
        offset = 0
        intLen = 0
    }

    operator fun compareTo(b: MutableBigInt): Int {
        val bLen = b.intLen
        if (intLen < bLen) return -1
        if (intLen > bLen) return 1

        val bVal = b.value
        var i = offset
        var j = b.offset
        while (i < intLen + offset) {
            val b1 = value[i++] + Int.MIN_VALUE
            val b2 = bVal[j++] + Int.MIN_VALUE
            if (b1 < b2) return -1
            if (b1 > b2) return 1
        }
        return 0
    }

    private fun compareShifted(b: MutableBigInt, ints: Int): Int {
        val bLen = b.intLen
        val aLen = intLen - ints
        if (aLen < bLen) return -1
        if (aLen > bLen) return 1

        val bVal = b.value
        var i = offset
        var j = b.offset
        while (i < intLen + offset) {
            val b1 = value[i++] + Int.MIN_VALUE
            val b2 = bVal[j++] + Int.MIN_VALUE
            if (b1 < b2) return -1
            if (b1 > b2) return 1
        }
        return 0
    }

    private fun compareHalf(b: MutableBigInt): Int {
        val bLen = b.intLen
        val len = intLen
        if (len <= 0) return if (bLen <= 0) 0 else -1
        if (len > bLen) return 1
        if (len < bLen - 1) return -1
        val bVal = b.value
        var bStart = 0
        var carry = 0

        if (len != bLen) {
            if (bVal[bStart] == 1) {
                ++bStart
                carry = 0x80000000.toInt()
            } else {
                return -1
            }
        }

        val value = value
        var i = offset
        var j = bStart
        while (i < len + offset) {
            val bv = bVal[j++]
            val hb = ((bv ushr 1) + carry).toLong() and LONG_MASK
            val v = value[i++].toLong() and LONG_MASK
            if (v != hb) return if (v < hb) -1 else 1
            carry = (bv and 1) shl 31
        }
        return if (carry == 0) 0 else -1
    }

    private fun getLowestSetBit(): Int {
        if (intLen == 0) return -1
        var j = intLen - 1
        while (j > 0 && value[offset + j] == 0) {
            --j
        }
        val b = value[offset + j]
        if (b == 0) return -1
        return ((intLen - 1 - j) shl 5) + b.countTrailingZeroBits()
    }

    private fun getInt(index: Int) = value[offset + index]
    private fun getLong(index: Int) = value[offset + index].toLong() and LONG_MASK

    internal fun normalize() {
        if (intLen == 0) {
            offset = 0
            return
        }
        var index = offset
        if (value[index] != 0) return
        var indexBound = index + intLen
        do {
            index++
        } while (index < indexBound && value[index] == 0)

        val numZeros = index - offset
        intLen -= numZeros
        offset = if (intLen == 0) 0 else offset + numZeros
    }

    private fun ensureCapacity(len: Int) {
        if (value.size < len) {
            value = IntArray(len)
            offset = 0
            intLen = len
        }
    }

    internal fun toIntArray() = IntArray(intLen) {
        value[offset + it]
    }

    internal fun setInt(index: Int, value: Int) {
        this.value[offset + index] = value
    }

    internal fun setValue(value: IntArray, length: Int) {
        this.value = value
        this.intLen = length
        this.offset = 0
    }

    internal fun copyValue(src: MutableBigInt) {
        val len = src.intLen
        if (value.size < len) {
            value = IntArray(len)
        }
        src.value.copyInto(value, 0, src.offset, src.offset + len)
        intLen = len
        offset = 0
    }

    internal fun copyValue(value: IntArray) {
        val len = value.size
        if (this.value.size < len) {
            this.value = IntArray(len)
        }
        value.copyInto(this.value, 0, 0, len)
        intLen = len
        offset = 0
    }

    override fun toString() = toBigInt(1).toString()

    internal fun safeRightShift(n: Int) = if (n / 32 >= intLen) reset() else rightShift(n)

    internal fun rightShift(n: Int) {
        if (intLen == 0) return
        val nInts = n ushr 5
        val nBits = n and 0x1F
        this.intLen -= nInts
        if (nBits == 0) return
        val bitsInHighWord = bitLengthForInt(value[offset])
        if (nBits >= bitsInHighWord) {
            primitiveLeftShift(32 - nBits)
            this.intLen--
        } else {
            primitiveRightShift(nBits)
        }
    }

    internal fun safeLeftShift(n: Int) {
        if (n > 0) leftShift(n)
    }

    internal fun leftShift(n: Int) {
        if (intLen == 0) return
        val nInts = n ushr 5
        val nBits = n and 0x1F
        val bitsInHighWord = bitLengthForInt(value[offset])

        if (n <= 32 - bitsInHighWord) {
            primitiveLeftShift(nBits)
            return
        }

        var newLen = intLen + nInts + 1
        if (nBits <= 32 - bitsInHighWord) {
            newLen--
        }
        if (value.size < newLen) {
            val result = IntArray(newLen)
            for (i in 0 until intLen) {
                result[i] = value[offset + i]
            }
            setValue(result, newLen)
        } else if (value.size - offset >= newLen) {
            for (i in 0 until newLen - intLen) {
                value[offset + intLen + i] = 0
            }
        } else {
            for (i in 0 until intLen) {
                value[i] = value[offset + i]
            }
            for (i in intLen until newLen) {
                value[i] = 0
            }
            offset = 0
        }
        intLen = newLen
        if (nBits == 0) return
        if (nBits <= 32 - bitsInHighWord) {
            primitiveLeftShift(nBits)
        } else {
            primitiveRightShift(nBits - (32 - bitsInHighWord))
        }
    }

    private fun divAdd(a: IntArray, result: IntArray, offset: Int): Int {
        var carry = 0L
        for (i in a.size - 1 downTo 0) {
            val sum = (a[i].toLong() and LONG_MASK) + (result[i + offset].toLong() and LONG_MASK) + carry
            result[i + offset] = sum.toInt()
            carry = sum ushr 32
        }
        return carry.toInt()
    }

    private fun mulSub(q: IntArray, a: IntArray, x: Int, len: Int, offset: Int): Int {
        val xLong = x.toLong() and LONG_MASK
        var carry = 0L
        var newOffset = offset + len

        for (i in len - 1 downTo 0) {
            val product = (a[i].toLong() and LONG_MASK) * xLong + carry
            val difference = q[newOffset] - product
            q[newOffset--] = difference.toInt()
            carry = (product ushr 32) +
                    if ((difference and LONG_MASK) > (product.toInt().inv().toLong() and LONG_MASK)) 1 else 0
        }

        return carry.toInt()
    }

    private fun mulSubBorrow(q: IntArray, a: IntArray, x: Int, len: Int, offset: Int): Int {
        val xLong = x.toLong() and LONG_MASK
        var carry = 0L
        val newOffset = offset + len

        for (i in len - 1 downTo 0) {
            val product = (a[i].toLong() and LONG_MASK) * xLong + carry
            val difference = q[newOffset] - product
            carry = (product ushr 32) +
                    if ((difference and LONG_MASK) > (product.toInt().inv().toLong() and LONG_MASK)) 1 else 0
        }

        return carry.toInt()
    }

    private fun primitiveRightShift(n: Int) {
        val value = value
        val n2 = 32 - n
        var i = offset + intLen - 1
        var c = value[i]
        while (i > offset) {
            val b = c
            c = value[i - 1]
            value[i] = (c shl n2) or (b ushr n)
            i--
        }
    }

    private fun primitiveLeftShift(n: Int) {
        val value = value
        val n2 = 32 - n
        var i = offset
        val c = value[i]
        val m = i + intLen - 1
        while (i < m) {
            val b = c
            val next = value[i + 1]
            value[i] = (b shl n) or (next ushr n2)
            i++
        }
        value[i] = value[i] shl n
    }

    private fun getLower(n: Int): BigInt {
        return if (isZero) BigInt.ZERO
        else if (intLen < n) toBigInt(1)
        else {
            var len = n
            while (len > 0 && value[offset + intLen - len] == 0) {
                len--
            }
            val sign = if (len > 0) 1 else 0
            BigInt(value.copyOfRange(offset + intLen - len, offset + intLen), sign)
        }
    }

    private fun keepLower(n: Int) {
        if (intLen > n) {
            offset += intLen - n
            intLen = n
        }
    }

    private fun add(addend: MutableBigInt) {
        TODO()
    }

    companion object {
        const val INFLATED = Long.MIN_VALUE
    }
}
