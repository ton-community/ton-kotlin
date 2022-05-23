package org.ton.cell

import org.ton.bigint.BigInt
import org.ton.bigint.bitLength
import org.ton.bigint.minus
import org.ton.bigint.times
import org.ton.bitstring.BitString

interface CellSlice {
    val bits: BitString
    val refs: List<Cell>
    val depth: Int

    /**
     * Checks if slice is empty. If not, throws an exception.
     */
    fun endParse()

    /**
     * Loads the first reference from the slice.
     */
    fun loadRef(): Cell
    fun <T> loadRef(cellSlice: CellSlice.() -> T): T
    fun preloadRef(): Cell
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

    operator fun <T : Any> invoke(cellSlice: CellSlice.() -> T): T = let(cellSlice)

    companion object {
        @JvmStatic
        fun beginParse(cell: Cell): CellSlice = CellSliceImpl(cell)
    }
}

private class CellSliceImpl(
    override val bits: BitString,
    override val refs: List<Cell>,
    private var bitsPosition: Int = 0,
    private var refsPosition: Int = 0
) : CellSlice {
    constructor(cell: Cell) : this(cell.bits, cell.references)

    override val depth: Int by lazy {
        refs.maxOfOrNull { it.maxDepth } ?: 0
    }

    override fun endParse() = check(bitsPosition == bits.length)

    override fun loadRef(): Cell {
        checkRefsOverflow()
        val cell = preloadRef()
        refsPosition++
        return cell
    }

    override fun <T> loadRef(cellSlice: CellSlice.() -> T): T {
        val slice = loadRef().beginParse()
        val result = cellSlice(slice)
        slice.endParse()
        return result
    }

    override fun preloadRef(): Cell = refs[refsPosition]

    override fun <T> preloadRef(cellSlice: CellSlice.() -> T): T {
        val slice = preloadRef().beginParse()
        val result = cellSlice(slice)
        slice.endParse()
        return result
    }

    override fun loadBit(): Boolean {
        val bit = preloadBit()
        bitsPosition++
        return bit
    }

    override fun preloadBit(): Boolean = bits[bitsPosition]

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
            bits[bitsPosition + index]
        }
    }

    override fun loadInt(length: Int): BigInt {
        val int = preloadInt(length)
        bitsPosition += length
        return int
    }

    override fun preloadInt(length: Int): BigInt {
        val uint = preloadUInt(length)
        val int = BigInt(1 shl (length - 1))
        return if (uint >= int) uint - (int * 2) else uint
    }

    override fun loadUInt(length: Int): BigInt {
        val uint = preloadUInt(length)
        bitsPosition += length
        return uint
    }

    override fun preloadUInt(length: Int): BigInt {
        val bits = preloadBits(length)
        val intBits = bits.joinToString(separator = "") { if (it) "1" else "0" }
        return BigInt(intBits, 2)
    }

    private fun checkBitsOverflow(length: Int) {
        val remaining = bits.length - bitsPosition
        require(length <= remaining) {
            "Bits overflow. Can't load $length bits. $remaining bits left."
        }
    }

    private fun checkRefsOverflow() {
        val remaining = 4 - refsPosition
        require(1 <= remaining) {
            "Refs overflow. Can't load ref. $remaining refs left."
        }
    }
}
