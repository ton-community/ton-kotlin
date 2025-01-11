package org.ton.bitstring

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
            setBitsAt(index, value.bytes, value.size)
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

    override fun setBitsAt(index: Int, value: ByteArray, bits: Int) {
        if (bits == 0) return
        val q = index / 8
        val r = index % 8

        if (r == 0) {
            val byteLen = ((bits + 7) / 8)
            value.copyInto(bytes, q, 0, byteLen)
            val shift = bits % 8
            if (shift != 0) {
                val mask = (0xFF shl (8 - shift)).toByte()
                val lastByteIndex = q + byteLen - 1
                bytes[lastByteIndex] = bytes[lastByteIndex] and mask
            }
        } else {
            val byteLen = ((bits + r + 7) / 8) - 1
            val valueLen = (bits + 7) / 8
            val shift = 8 - r
            for (i in 0 until byteLen) {
                var bytesIndex = q + i
                val valueByte = value[i].toInt() and 0xFF
                bytes[bytesIndex] = bytes[bytesIndex] or (valueByte ushr r).toByte()
                bytes[bytesIndex + 1] = (valueByte shl shift).toByte()
            }
            val lastByteIndex = q + byteLen
            if (byteLen < valueLen) {
                val valueByte = value[byteLen].toInt() and 0xFF
                bytes[lastByteIndex] = bytes[lastByteIndex] or (valueByte ushr r).toByte()
            }
            val bitsR = (r + bits) % 8
            if (bitsR != 0) {
                val mask = (0xFF shl (8 - bitsR)).toByte()
                bytes[lastByteIndex] = bytes[lastByteIndex] and mask
            }
        }
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
        require(value.sign >= 0) { "Integer `$value` must be unsigned" }
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

/*
    override fun plus(bits: BooleanArray): ByteBackedMutableBitString = plus(bits.asIterable())
    override fun plus(bytes: ByteArray): ByteBackedMutableBitString = plus(bytes, bytes.size * Byte.SIZE_BITS)
    override fun plus(bits: Iterable<Boolean>): ByteBackedMutableBitString =
        plus(if (bits is Collection<Boolean>) bits else bits.toList())

    override fun plus(bits: BitString): BitString {
        return if (bits is ByteBackedBitString) {
            plus(bits)
        } else {
            plus(bits.toList())
        }
    }

    public fun plus(bits: ByteBackedBitString): BitString = plus(bits.bytes, bits.size)

    override fun plus(bits: Collection<Boolean>): ByteBackedMutableBitString = apply {
        val bitsCount = bits.size

        val newBytes = expandByteArray(bytes, size + bitsCount)
        bits.forEachIndexed { index, bit ->
            set(newBytes, size + index, bit)
        }
        bytes = newBytes
        size += bitsCount
    }

    override fun plus(bit: Boolean): MutableBitString = plus(listOf(bit))

    override fun plus(bytes: ByteArray, bits: Int): ByteBackedMutableBitString = apply {
        if (bits != 0) {
            if (size % 8 == 0) {
                if (bits % 8 == 0) {
                    appendWithoutShifting(bytes, bits)
                } else {
                    appendWithShifting(bytes, bits)
                }
            } else {
                appendWithDoubleShifting(bytes, bits)
            }
        }
    }

    private fun appendWithoutShifting(byteArray: ByteArray, bits: Int) {
        require(size % 8 == 0)
        require(bits % 8 == 0)

        val newBytes = expandByteArray(bytes, size + bits)
        byteArray.copyInto(
            destination = newBytes,
            destinationOffset = size / Byte.SIZE_BITS,
            endIndex = bits / Byte.SIZE_BITS
        )
        bytes = newBytes
        size += bits
    }

    private fun appendWithShifting(byteArray: ByteArray, bits: Int) {
        require(size % 8 == 0)
        val shift = bits % 8
        require(shift != 0)

        val newBytes = expandByteArray(bytes, size + bits)
        byteArray.copyInto(
            destination = newBytes,
            destinationOffset = size / Byte.SIZE_BITS,
            endIndex = bits / Byte.SIZE_BITS + 1
        )
        var lastByte = byteArray[bits / Byte.SIZE_BITS].toInt()
        lastByte = lastByte shr (8 - shift)
        lastByte = lastByte shl (8 - shift)
        newBytes[(size + bits) / Byte.SIZE_BITS] = lastByte.toByte()
        bytes = newBytes
        size += bits
    }

    private fun appendWithDoubleShifting(byteArray: ByteArray, bits: Int) {
        val selfShift = size % 8
        val data = bytes.copyOf(size / 8 + byteArray.size + 1)
        val lastIndex = size / 8
        val lastBits = data[lastIndex].toInt() shr (8 - selfShift)
        var y = lastBits
        byteArray.forEachIndexed { i, x ->
            y = (y shl 8) or (x.toInt() and 0xFF)
            val newByte = y shr selfShift
            data[lastIndex + i] = newByte.toByte()
        }
        val a = lastIndex + byteArray.size
        data[a] = (y shl (8 - selfShift)).toByte()

        val newSize = size + bits
        val shift = newSize % 8
        if (shift == 0) {
            val newBytes = expandByteArray(data, newSize)
            bytes = newBytes
            size = newSize
        } else {
            val newBytes = expandByteArray(data, newSize)
            var lastByte = newBytes[newBytes.lastIndex].toInt()
            lastByte = lastByte shr (8 - shift)
            lastByte = lastByte shl (8 - shift)
            newBytes[newBytes.lastIndex] = lastByte.toByte()
            bytes = newBytes
            size = newSize
        }
    }
 */
