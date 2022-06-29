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

    fun loadBits(length: Int): BooleanArray
    fun preloadBits(length: Int): BooleanArray

    fun loadBitString(length: Int): BitString
    fun preloadBitString(length: Int): BitString

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

    override fun loadBits(length: Int): BooleanArray {
        val bits = preloadBits(length)
        bitsPosition += length
        return bits
    }

    override fun loadBitString(length: Int): BitString {
        val bitString = preloadBitString(length)
        bitsPosition += length
        return bitString
    }

    override fun preloadBitString(length: Int): BitString = BitString(*preloadBits(length))

    override fun preloadBits(length: Int): BooleanArray {
        checkBitsOverflow(length)
        return BooleanArray(length) { index ->
            try {
                bits[bitsPosition + index]
            } catch (e: BitStringUnderflowException) {
                throw CellUnderflowException(e)
            }
        }
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
        // TODO: optimize with bit operations
        val intBits = bits.joinToString(separator = "") { if (it) "1" else "0" }
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
    val data = bits.bytes

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

    override fun preloadBitString(length: Int): BitString {
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

    override fun preloadBits(length: Int): BooleanArray = preloadBitString(length).toBooleanArray()
}