package org.ton.bitstring

import kotlinx.io.bytestring.ByteString
import org.ton.bigint.BigInt
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.jvm.JvmStatic

public open class ByteBackedMutableBitString(
    override var bytes: ByteArray,
    override var size: Int
) : ByteBackedBitString(size, bytes), MutableBitString {
    override operator fun set(index: Int, bit: Int) {
        set(index, bit != 0)
    }

    override operator fun set(index: Int, element: Boolean): Boolean {
        return set(bytes, index, element)
    }

    override fun setBitsAt(index: Int, value: BitString) {
        if (value is ByteBackedBitString) {
            bitsCopy(bytes, index, value.bytes, 0, value.size)
        } else {
            if (value.size == 0) return
            value.forEachIndexed { i, bit ->
                set(index + i, bit)
            }
        }
    }

    override fun setBitsAt(index: Int, value: Iterable<Boolean>) {
        value.forEachIndexed { i, bit ->
            set(index + i, bit)
        }
    }

    override fun setBitsAt(index: Int, value: ByteArray, bitCount: Int) {
        bitsCopy(bytes, index, value, 0, bitCount)
    }

    override fun setBitsAt(index: Int, value: ByteString, bitCount: Int) {
        bitsCopy(bytes, index, value.toByteArray(), 0, bitCount)
    }

    override fun setBigIntAt(index: Int, value: BigInt, bits: Int) {
        if (bits == 0) {
            return
        }
        val bits = bits - 1
        if (value.sign == -1) {
            set(index, true)
            val index = index + 1
            val newValue = (BigInt.ONE shl bits) + value
            for (i in 0 until bits) {
                val bit = newValue.bitAt(bits - i - 1)
                set(index + i, bit)
            }
        } else {
            set(index, false)
            val index = index + 1
            for (i in 0 until bits) {
                val bit = value.bitAt(bits - i - 1)
                set(index + i, bit)
            }
        }
    }

    override fun setUBigIntAt(index: Int, value: BigInt, bits: Int) {
        check(value.bitLength <= bits) { "Integer `$value` does not fit into $bits bits" }
//        require(value.sign >= 0) { "Integer `$value` must be unsigned" }
        for (i in 0 until bits) {
            set(index + i, value.bitAt(bits - i - 1))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ByteBackedBitString) return false

        if (size != other.size) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + bytes.contentHashCode()
        return result
    }

    public companion object {
        @JvmStatic
        public fun of(size: Int = 0): ByteBackedMutableBitString {
            val bytes = constructByteArray(size)
            return ByteBackedMutableBitString(bytes, size)
        }

        @JvmStatic
        public fun of(byteArray: ByteArray, size: Int = byteArray.size * Byte.SIZE_BITS): ByteBackedMutableBitString {
            val bytes = constructByteArray(byteArray, size)
            return ByteBackedMutableBitString(bytes, size)
        }

        @JvmStatic
        public fun of(bitString: BitString, size: Int = bitString.size): ByteBackedMutableBitString {
            return if (bitString is ByteBackedBitString) {
                of(bitString.bytes, size)
            } else {
                val result = of(size)
                bitString.forEachIndexed { index, bit ->
                    result[index] = bit
                }
                result
            }
        }

        @JvmStatic
        protected fun set(bytes: ByteArray, index: Int, element: Boolean): Boolean {
            val wordIndex = index.byteIndex
            val bitMask = index.bitMask
            val previous = get(bytes, index)
            if (element) {
                bytes[wordIndex] = bytes[wordIndex] or bitMask
            } else {
                bytes[wordIndex] = bytes[wordIndex] and bitMask.inv()
            }
            return previous
        }
    }
}
