package org.ton.cell

import org.ton.bitstring.BitString
import org.ton.bitstring.Bits256

public class DataCell(
    override val descriptor: CellDescriptor,
    override val bits: BitString,
    override val refs: List<Cell>,
    private val hashes: List<Pair<ByteArray, Int>>
) : Cell {
    override fun hash(level: Int): Bits256 {
        val hashIndex = levelMask.apply(level).hashIndex
        return Bits256(hashes[hashIndex].first)
    }

    override fun depth(level: Int): Int {
        val hashIndex = levelMask.apply(level).hashIndex
        return hashes[hashIndex].second
    }

    override fun virtualize(offset: Int): Cell {
        return if (levelMask.isEmpty()) {
            this
        } else {
            VirtualCell(this, offset)
        }
    }

    override fun toString(): String = Cell.toString(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataCell) return false

        if (descriptor != other.descriptor) return false
        if (bits != other.bits) return false
        return refs == other.refs
    }

    override fun hashCode(): Int {
        var result = descriptor.hashCode()
        result = 31 * result + bits.hashCode()
        result = 31 * result + refs.hashCode()
        return result
    }
}
