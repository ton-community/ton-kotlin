package org.ton.cell

import org.ton.bitstring.BitString
import org.ton.bitstring.BitStringReader
import org.ton.primitives.BigInt
import kotlin.jvm.JvmStatic

interface CellSlice {

    val refs: Int
    val bits: Int
    val bitsRefs: Int
    val depth: Int
    val isDataEmpty: Boolean
    val isRefsEmpty: Boolean
    val isEmpty: Boolean get() = isDataEmpty && isRefsEmpty

    /**
     * Checks if slice is empty. If not, throws an exception.
     */
    fun endParse()

    /**
     * Loads the first reference from the slice.
     */
    fun loadRef(): Cell
    fun preloadRef(): Cell

    fun loadInt(length: Int): BigInt
    fun loadUInt(length: Int): BigInt

    fun preloadInt(length: Int): BigInt
    fun preloadUInt(length: Int): BigInt

    fun loadBits(length: Int): CellSlice
    fun preloadBits(length: Int): CellSlice

    fun loadGrams(): BigInt

    fun skipBits(length: Int): CellSlice

    fun firstBits(length: Int): CellSlice

    fun skipLastBits(length: Int): CellSlice

    fun sliceLast(length: Int): CellSlice

    fun loadDict(): Cell

    fun preloadDict(): Cell

    fun skipDict(): Cell

    /**
     * Computes the hash of a slice s and returns it as a 256-bit byte array.
     * The result is the same as if an ordinary cell containing only data and references from slice
     * had been created and its hash computed by [Cell.hash].
     */
    fun hash(): ByteArray

    companion object {
        @JvmStatic
        fun beginParse(cell: Cell): CellSlice = CellSliceImpl(cell)
    }
}

private class CellSliceImpl(
    val data: BitString,
    val references: List<Cell>
) : CellSlice {
    constructor(cell: Cell) : this(cell.data, cell.references)

    private val bitStringReader = BitStringReader(data)
    private var referenceIndex = 0

    override val refs: Int
        get() = references.size
    override val bits: Int
        get() = data.length
    override val bitsRefs: Int
        get() = TODO("Not yet implemented")
    override val depth: Int
        get() = TODO("Not yet implemented")
    override val isDataEmpty: Boolean
        get() = TODO("Not yet implemented")
    override val isRefsEmpty: Boolean
        get() = TODO("Not yet implemented")

    override fun endParse() = check(bitStringReader.remaining == 0)

    override fun loadRef(): Cell {
        val cell = preloadRef()
        referenceIndex++
        return cell
    }

    override fun preloadRef(): Cell = references[referenceIndex]

    override fun loadInt(length: Int): BigInt = bitStringReader.readInt(length)

    override fun loadUInt(length: Int): BigInt = bitStringReader.readUInt(length)

    override fun preloadInt(length: Int): BigInt {
        val int = bitStringReader.readInt(length)
        bitStringReader.offset -= length
        return int
    }

    override fun preloadUInt(length: Int): BigInt {
        val int = bitStringReader.readUInt(length)
        bitStringReader.offset -= length
        return int
    }

    override fun loadBits(length: Int): CellSlice = CellSliceImpl(bitStringReader.readBits(length), references)

    override fun preloadBits(length: Int): CellSlice {
        val slice = loadBits(length)
        bitStringReader.offset -= length
        return slice
    }

    override fun loadGrams(): BigInt {
        TODO("Not yet implemented")
    }

    override fun skipBits(length: Int): CellSlice = apply {
        bitStringReader.offset += length
    }

    override fun firstBits(length: Int): CellSlice {
        TODO("Not yet implemented")
    }

    override fun skipLastBits(length: Int): CellSlice {
        TODO("Not yet implemented")
    }

    override fun sliceLast(length: Int): CellSlice {
        TODO("Not yet implemented")
    }

    override fun loadDict(): Cell {
        TODO("Not yet implemented")
    }

    override fun preloadDict(): Cell {
        TODO("Not yet implemented")
    }

    override fun skipDict(): Cell {
        TODO("Not yet implemented")
    }

    override fun hash(): ByteArray {
        TODO("Not yet implemented")
    }
}