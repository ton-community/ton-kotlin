@file:Suppress("NOTHING_TO_INLINE")

package org.ton.cell

import kotlinx.io.bytestring.ByteString
import org.ton.bigint.BigInt
import org.ton.bigint.toBigInt
import org.ton.bigint.toULong
import org.ton.bitstring.BitString
import org.ton.bitstring.ByteBackedBitString
import org.ton.bitstring.exception.BitStringUnderflowException
import org.ton.cell.exception.CellUnderflowException
import kotlin.jvm.JvmStatic

public inline fun CellSlice(bits: BitString, refs: List<Cell> = emptyList()): CellSlice = CellSlice.of(bits, refs)

public interface CellSlice {
    public val bits: BitString
    public val refs: List<Cell>
    public var bitsPosition: Int
    public var refsPosition: Int
    public val remainingBits: Int get() = bits.size - bitsPosition

    /**
     * Checks if slice is empty. If not, throws an exception.
     */
    public fun endParse()

    /**
     * Loads the first reference from the slice.
     */
    public fun loadRef(): Cell
    public fun loadRefs(count: Int): List<Cell>

    public fun preloadRef(): Cell
    public fun preloadRefs(count: Int): List<Cell>
    public fun <T> preloadRef(cellSlice: CellSlice.() -> T): T

    public fun loadBit(): Boolean
    public fun preloadBit(): Boolean

    public fun skipBits(bitLength: Int): CellSlice

    public fun loadBits(bitLength: Int): BitString
    public fun preloadBits(bitLength: Int): BitString

    public fun loadByteArray(bitLength: Int): ByteArray
    public fun preloadByteArray(bitLength: Int): ByteArray

    public fun loadByteString(bitLength: Int): ByteString = ByteString(* loadByteArray(bitLength))
    public fun preloadByteString(bitLength: Int): ByteString = ByteString(*preloadByteArray(bitLength))

    public fun loadBigInt(bitLength: Int): BigInt
    public fun preloadBigInt(bitLength: Int): BigInt

    public fun loadInt(bitLength: Int = Int.SIZE_BITS): Int = loadLong(bitLength).toInt()
    public fun preloadInt(bitLength: Int = Int.SIZE_BITS): Int = preloadLong(bitLength).toInt()

    public fun loadLong(bitLength: Int = Long.SIZE_BITS): Long = loadBigInt(bitLength).toLong()
    public fun preloadLong(bitLength: Int = Long.SIZE_BITS): Long = preloadBigInt(bitLength).toLong()

    public fun loadUBigInt(bitLength: Int): BigInt
    public fun preloadUBigInt(bitLength: Int): BigInt

    public fun loadVarUInt(maxByteLength: Int): BigInt
    public fun preloadVarUInt(maxByteLength: Int): BigInt

    public fun loadUInt(bitLength: Int = UInt.SIZE_BITS): UInt = loadULong(bitLength).toUInt()
    public fun preloadUInt(bitLength: Int = UInt.SIZE_BITS): UInt = preloadULong(bitLength).toUInt()

    public fun loadULong(bitLength: Int = ULong.SIZE_BITS): ULong = loadUBigInt(bitLength).toULong()
    public fun preloadULong(bitLength: Int = ULong.SIZE_BITS): ULong = preloadUBigInt(bitLength).toULong()

    public fun loadUIntLeq(max: Int): UInt {
        val i = loadUInt(Int.SIZE_BITS - max.countLeadingZeroBits())
        return i
    }

    public fun preloadUIntLeq(max: Int): UInt = preloadUInt(Int.SIZE_BITS - max.countLeadingZeroBits())

    public fun loadUIntLes(max: Int): UInt = loadUIntLeq(max - 1)
    public fun preloadUIntLes(max: Int): UInt = loadUIntLeq(max - 1)

    public fun isEmpty(): Boolean = bits.isEmpty() && refs.isEmpty()

    public operator fun component1(): BitString = bits
    public operator fun component2(): List<Cell> = refs

    public companion object {
        @JvmStatic
        public fun beginParse(cell: Cell): CellSlice {
            return of(cell.bits, cell.refs)
        }

        @JvmStatic
        public fun of(bits: BitString, refs: List<Cell> = emptyList()): CellSlice {
            return if (bits is ByteBackedBitString) {
                CellSliceByteBackedBitString(bits, refs)
            } else {
                CellSliceImpl(bits, refs)
            }
        }
    }
}

public inline operator fun <T> CellSlice.invoke(cellSlice: CellSlice.() -> T): T = let(cellSlice)

public inline fun <T> CellSlice.loadRef(cellSlice: CellSlice.() -> T): T =
    cellSlice(loadRef().beginParse())

private open class CellSliceImpl(
    override val bits: BitString,
    override val refs: List<Cell>,
    override var bitsPosition: Int = 0,
    override var refsPosition: Int = 0
) : CellSlice {
    override fun endParse() =
        check(bitsPosition >= bits.size) { "bitsPosition: $bitsPosition != bits.length: ${bits.size}" }

    override fun loadRef(): Cell {
        checkRefsOverflow()
        val cell = preloadRef()
        refsPosition++
        return cell
    }

    override fun loadRefs(count: Int): List<Cell> = List(count) { loadRef() }

    override fun preloadRef(): Cell = refs[refsPosition]

    override fun <T> preloadRef(cellSlice: CellSlice.() -> T): T {
        val slice = preloadRef().beginParse()
        return cellSlice(slice)
    }

    override fun preloadRefs(count: Int): List<Cell> = List(refsPosition + count) { refs[it] }

    override fun loadBit(): Boolean {
        val bit = preloadBit()
        bitsPosition++
        return bit
    }

    override fun preloadBit(): Boolean = try {
        bits[bitsPosition]
    } catch (e: BitStringUnderflowException) {
        throw CellUnderflowException(e)
    }

    override fun skipBits(bitLength: Int): CellSlice = apply {
        bitsPosition += bitLength
    }

    override fun loadBits(bitLength: Int): BitString {
        val bitString = preloadBits(bitLength)
        bitsPosition += bitLength
        return bitString
    }

    override fun preloadBits(bitLength: Int): BitString {
        checkBitsOverflow(bitLength)
        return bits.slice(bitsPosition..bitLength)
    }

    override fun loadByteArray(bitLength: Int): ByteArray {
        val array = preloadByteArray(bitLength)
        bitsPosition += bitLength
        return array
    }

    override fun preloadByteArray(bitLength: Int): ByteArray {
        val bitString = preloadBits(bitLength)
        return bitString.toByteArray()
    }

    override fun loadBigInt(bitLength: Int): BigInt {
        val int = preloadBigInt(bitLength)
        bitsPosition += bitLength
        return int
    }

    override fun preloadBigInt(bitLength: Int): BigInt {
        val uint = preloadUBigInt(bitLength)
        val int = BigInt.ONE shl (bitLength - 1)
        return if (uint >= int) uint - (int * BigInt.TWO) else uint
    }

    override fun loadUBigInt(bitLength: Int): BigInt {
        val uint = preloadUBigInt(bitLength)
        bitsPosition += bitLength
        return uint
    }

    override fun preloadUBigInt(bitLength: Int): BigInt {
        if (bitLength == 0) return BigInt.ZERO
        val bits = preloadBits(bitLength)
        val intBits = buildString(bitLength) {
            bits.forEach { bit ->
                if (bit) {
                    append('1')
                } else {
                    append('0')
                }
            }
        }
        return BigInt(intBits, 2)
    }

    override fun loadVarUInt(maxByteLength: Int): BigInt {
        val bits = loadUIntLes(maxByteLength).toInt() * Byte.SIZE_BITS
        return loadUBigInt(bits)
    }

    override fun preloadVarUInt(maxByteLength: Int): BigInt {
        val tmp = bitsPosition
        val value = loadVarUInt(maxByteLength)
        bitsPosition = tmp
        return value
    }

    fun checkBitsOverflow(length: Int) {
        val remaining = bits.size - bitsPosition
        require(length <= remaining) {
            "Bits overflow. Can't load $length bits. $remaining bits left."
        }
    }

    fun checkRefsOverflow() {
        val remaining = 4 - refsPosition
        require(1 <= remaining) {
            "Refs overflow. Can't load ref. $remaining refs left."
        }
    }

    override fun toString(): String = buildString {
        append("x")
        append(bits.toString())
        if (refs.isNotEmpty()) {
            append(",")
            append(refs.size)
        }
    }
}

private class CellSliceByteBackedBitString(
    override val bits: ByteBackedBitString,
    refs: List<Cell>
) : CellSliceImpl(bits, refs) {
    val data get() = bits.bytes

    fun getBits(offset: Int, length: Int): Byte {
        val index = bitsPosition + offset
        val q = index / 8
        val r = index % 8
        val result = if (r == 0) {
            (data[q].toInt() and 0xFF) shr (8 - length)
        } else if (length <= (8 - r)) {
            ((data[q].toInt() and 0xFF) shr (8 - r - length)) and ((1 shl length) - 1)
        } else {
            var ret = 0
            if (q < data.size) {
                ret = ret or ((data[q].toInt() and 0xFF) shl 8)
            }
            if (q < data.size - 1) {
                ret = ret or (data[q + 1].toInt() and 0xFF)
            }
            (ret shr (8 - r)).toByte().toInt() shr (8 - length)
        }
        return result.toByte()
    }

    fun getByte(offset: Int) = getBits(offset, 8)

    fun getULong(length: Int): ULong {
        if (length == 0) return 0uL
        var value = 0uL
        val bytes = length / 8
        val remainder = length % 8
        repeat(bytes) { i ->
            val byte = getByte(8 * i).toInt() and 0xFF
            value = value or (byte.toULong() shl (8 * (7 - i)))
        }
        if (remainder != 0) {
            val r = getBits(bytes * 8, remainder).toInt() and 0xFF
            value = value or (r.toULong() shl (8 * (7 - bytes) + (8 - remainder)))
        }
        return value shr (64 - length)
    }

    override fun preloadBits(bitLength: Int): BitString {
        val array = preloadByteArray(bitLength)
        return BitString(array, bitLength)
    }

    override fun preloadByteArray(bitLength: Int): ByteArray {
        val bytes = bitLength / 8
        val remainder = bitLength % 8
        val arraySize = bytes + if (remainder != 0) 1 else 0
        val array = ByteArray(arraySize)
        if (bitsPosition % 8 == 0) {
            val startIndex = bitsPosition / 8
            data.copyInto(array, startIndex = startIndex, endIndex = startIndex + bytes)
        } else {
            repeat(bytes) { i ->
                array[i] = getByte(i * 8)
            }
        }
        if (remainder != 0) {
            val v = getBits(bytes * 8, remainder).toInt() shl (8 - remainder)
            array[array.lastIndex] = v.toByte()
        }
        return array
    }

    override fun preloadUBigInt(bitLength: Int): BigInt {
        return when {
            bitLength == 0 -> BigInt.ZERO
            bitLength > 64 -> super.preloadUBigInt(bitLength)
            bitLength == 8 -> {
                val byte = getByte(0).toInt() and 0xFF
                byte.toBigInt()
            }

            else -> {
                val value = getULong(bitLength)
                if (value > Long.MAX_VALUE.toULong()) {
                    BigInt(value.toString(), 10)
                } else {
                    value.toBigInt()
                }
            }
        }
    }

    override fun preloadBigInt(bitLength: Int): BigInt {
        return when {
            bitLength == 0 -> BigInt.ZERO
            bitLength > 64 -> super.preloadBigInt(bitLength)
            else -> {
                val uint = getULong(bitLength).toLong()
                val int = 1L shl (bitLength - 1)
                if (uint >= int) {
                    (uint - (int * 2)).toBigInt()
                } else {
                    uint.toBigInt()
                }
            }
        }
    }

    override fun loadLong(bitLength: Int): Long {
        val value = preloadLong(bitLength)
        bitsPosition += bitLength
        return value
    }

    override fun loadULong(bitLength: Int): ULong {
        val value = preloadULong(bitLength)
        bitsPosition += bitLength
        return value
    }

    override fun preloadLong(bitLength: Int): Long = preloadULong(bitLength).toLong()

    override fun preloadULong(bitLength: Int): ULong {
        return when {
            bitLength == 0 -> 0uL
            bitLength <= 64 -> getULong(bitLength)
            else -> throw IllegalArgumentException("expected length in 0..64, actual: $bitLength")
        }
    }
}
