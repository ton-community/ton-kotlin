@file:Suppress("OPT_IN_USAGE")

package org.ton.cell

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.cell.exception.CellOverflowException

fun Cell(hex: String, vararg refs: Cell): Cell =
    Cell.of(hex, *refs)

fun Cell(bits: BitString = BitString(), refs: Iterable<Cell> = emptyList(), type: CellType = CellType.ORDINARY) =
    Cell.of(bits, refs, type)

fun Cell(bits: BitString, vararg refs: Cell): Cell =
    Cell.of(bits, *refs)

@JsonClassDiscriminator("@type")
interface Cell {
    val bits: BitString
    val refs: List<Cell>
    val cellType: CellType

    val isExotic: Boolean
    val isMerkle: Boolean
    val isPruned: Boolean
    val maxDepth: Int
    val levelMask: LevelMask

    fun isEmpty(): Boolean = bits.isEmpty() && refs.isEmpty()

    fun treeWalk(): Sequence<Cell>
    fun loadCell(): Cell
    fun beginParse(): CellSlice

    fun <T> parse(block: CellSlice.() -> T): T {
        val slice = beginParse()
        val result = block(slice)
        slice.endParse()
        return result
    }

    /**
     * Computes the representation hash of a cell and returns it as a 256-bit byte array.
     * Useful for signing and checking signatures of arbitrary entities represented by a tree of cells.
     */
    fun hash(): ByteArray

    fun descriptors(): ByteArray

    override fun toString(): String

    companion object {
        const val MAX_REFS = 4

        @JvmStatic
        fun of(hex: String, vararg refs: Cell): Cell =
            DataCell.of(BitString(hex), *refs)

        @JvmStatic
        fun of(
            bits: BitString = BitString(),
            refs: Iterable<Cell> = emptyList(),
            type: CellType = CellType.ORDINARY
        ): Cell = DataCell.of(bits, refs, type)

        @JvmStatic
        fun of(
            bits: BitString,
            vararg refs: Cell
        ): Cell = DataCell.of(bits, *refs)

        @JvmStatic
        fun checkRefsCount(count: Int, range: IntRange =  0..MAX_REFS) = require(count in range) {
            throw CellOverflowException("Refs overflow. expected: $range, actual: $count")
        }

        @JvmStatic
        fun toString(
            cell: Cell,
            appendable: Appendable,
            indent: String = "",
            lastChild: Boolean = true,
            firstChild: Boolean = true
        ) {
            appendable.append(indent)
            if (cell.isExotic) {
                appendable.append(cell.cellType.toString())
                appendable.append(' ')
            }
            appendable.append("x{")
            appendable.append(cell.bits.toString())
            appendable.append("}")
            cell.refs.forEachIndexed { index, reference ->
                appendable.append('\n')
                val firstRef = index == 0
                val lastRef = index == cell.refs.lastIndex
                toString(reference, appendable, "$indent    ", firstRef, lastRef)
            }
        }
    }
}

