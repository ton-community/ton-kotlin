package org.ton.cell

import org.ton.bigint.*
import org.ton.bitstring.BitString
import org.ton.bitstring.ByteBackedBitString
import org.ton.bitstring.exception.BitStringUnderflowException
import org.ton.cell.exception.CellUnderflowException

interface CellSlice {
    val bits: BitString
    val refs: List<Cell>
    val depth: Int
    var bitsPosition: Int
    var refsPosition: Int

    /**
     * Checks if slice is empty. If not, throws an exception.
     */
    fun endParse()

    /**
     * Loads the first reference from the slice.
     */
    fun loadRef(): Cell
    fun loadRefs(count: Int): List<Cell>

    fun preloadRef(): Cell
    fun preloadRefs(count: Int): List<Cell>
    fun <T> preloadRef(cellSlice: CellSlice.() -> T): T

    fun loadBit(): Boolean
    fun preloadBit(): Boolean

    fun skipBits(length: Int): CellSlice

    fun loadBits(length: Int): BitString
    fun preloadBits(length: Int): BitString

    fun loadInt(length: Int): BigInt
    fun preloadInt(length: Int): BigInt

    fun loadUInt(length: Int): BigInt
    fun preloadUInt(length: Int): BigInt

    fun loadUIntLeq(max: Int) = loadUInt(BigInt(max).bitLength)
    fun preloadUIntLeq(max: Int) = loadUInt(BigInt(max).bitLength)

    fun loadUIntLes(max: Int) = loadUInt(BigInt(max - 1).bitLength)
    fun preloadUIntLes(max: Int) = loadUInt(BigInt(max - 1).bitLength)

    fun isEmpty(): Boolean = bits.isEmpty() && refs.isEmpty()

    operator fun component1(): BitString = bits
    operator fun component2(): List<Cell> = refs


    companion object {
        @JvmStatic
        fun beginParse(cell: Cell): CellSlice = of(cell.bits, cell.refs)

        @JvmStatic
        fun of(bits: BitString, refs: List<Cell>): CellSlice {
            return if (bits is ByteBackedBitString) {
                CellSliceByteBackedBitString(bits, refs)
            } else {
                CellSliceImpl(bits, refs)
            }
        }
    }
}

inline operator fun <T> CellSlice.invoke(cellSlice: CellSlice.() -> T): T = let(cellSlice)

inline fun <T> CellSlice.loadRef(cellSlice: CellSlice.() -> T): T =
    cellSlice(loadRef().beginParse())

private open class CellSliceImpl(
    override val bits: BitString,
    override val refs: List<Cell>,
    override var bitsPosition: Int = 0,
    override var refsPosition: Int = 0
) : CellSlice {

    override val depth: Int by lazy {
        refs.maxOfOrNull { it.maxDepth } ?: 0
    }

    override fun endParse() =
        check(bitsPosition == bits.size) { "bitsPosition: $bitsPosition != bits.length: ${bits.size}" }

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

    override fun skipBits(length: Int): CellSlice = apply {
        bitsPosition += length
    }

    override fun loadBits(length: Int): BitString {
        val bitString = preloadBits(length)
        bitsPosition += length
        return bitString
    }

    override fun preloadBits(length: Int): BitString {
        checkBitsOverflow(length)
        return bits.slice(bitsPosition..length)
    }

    override fun loadInt(length: Int): BigInt {
        val int = preloadInt(length)
        bitsPosition += length
        return int
    }

    override fun preloadInt(length: Int): BigInt {
        val uint = preloadUInt(length)
        val int = BigInt(1) shl (length - 1)
        return if (uint >= int) uint - (int * 2) else uint
    }

    override fun loadUInt(length: Int): BigInt {
        val uint = preloadUInt(length)
        bitsPosition += length
        return uint
    }

    override fun preloadUInt(length: Int): BigInt {
        if (length == 0) return BigInt(0)
        val bits = preloadBits(length)
        val intBits = buildString(length) {
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

    protected fun checkBitsOverflow(length: Int) {
        val remaining = bits.size - bitsPosition
        require(length <= remaining) {
            "Bits overflow. Can't load $length bits. $remaining bits left."
        }
    }

    protected fun checkRefsOverflow() {
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

    fun getLong(length: Int): ULong {
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

    override fun preloadBits(length: Int): BitString {
        val bytes = length / 8
        val remainder = length % 8
        val arraySize = bytes + if (remainder != 0) 1 else 0
        val array = ByteArray(arraySize)
        repeat(bytes) { i ->
            array[i] = getByte(i * 8)
        }
        if (remainder != 0) {
            val v = getBits(bytes * 8, remainder).toInt() shl (8 - remainder)
            array[array.lastIndex] = v.toByte()
        }
        return BitString(array, length)
    }

    override fun preloadUInt(length: Int): BigInt {
        return when {
            length == 0 -> 0.toBigInt()
            length > 64 -> super.preloadUInt(length)
            length == 8 -> {
                val byte = getByte(0).toInt() and 0xFF
                byte.toBigInt()
            }
            else -> {
                val value = getLong(length)
                if (value > Long.MAX_VALUE.toULong()) {
                    BigInt(value.toString(), 10)
                } else {
                    BigInt(value.toLong())
                }
            }
        }
    }

    override fun preloadInt(length: Int): BigInt {
        return when {
            length == 0 -> 0.toBigInt()
            length > 64 -> super.preloadInt(length)
            else -> {
                val uint = getLong(length).toLong()
                val int = 1L shl (length - 1)
                if (uint >= int) {
                    BigInt(uint - (int * 2))
                } else {
                    BigInt(uint)
                }
            }
        }
    }
}