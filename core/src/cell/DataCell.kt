package org.ton.cell

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.hexToByteString
import kotlinx.io.bytestring.toHexString
import org.ton.bitstring.BitString

public class DataCell internal constructor(
    public val descriptor: CellDescriptor,
    public val bits: BitString,
    public val refs: List<Cell>,
    internal val hashes: Array<ByteString>,
    internal val depths: IntArray
) : Cell {
    private var hashCode: Int = 0

    override val levelMask: LevelMask = descriptor.levelMask
    public val type: CellType get() = descriptor.cellType

    override fun hash(level: Int): ByteString {
        val hashIndex = levelMask.apply(level).hashIndex
        return hashes[hashIndex]
    }

    override fun depth(level: Int): Int {
        val hashIndex = levelMask.apply(level).hashIndex
        return depths[hashIndex]
    }

    override fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(refs)
        refs.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    override fun virtualize(offset: Int): Cell {
        return if (levelMask.isEmpty()) {
            this
        } else {
            VirtualCell(this, offset)
        }
    }

    public fun beginParse(): CellSlice = CellSlice(this)

    public fun asCellSlice(): CellSlice = CellSlice(this)

    public fun <T> parse(block: CellSlice.() -> T): T {
        val slice = beginParse()
        val result = block(slice)
        return result
    }

    override fun toString(): String = "Cell($type, data=$bits, bits=${bits.size}, hash=${hash().toHexString()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataCell) return false
        if (hashCode != 0 && other.hashCode != 0 && hashCode != other.hashCode) return false
        return hash() == other.hash()
    }

    override fun hashCode(): Int {
        var hc = hashCode
        if (hc == 0) {
            hc = hash().hashCode()
            hashCode = hc
        }
        return hc
    }

    public companion object {
        public val EMPTY: DataCell = DataCell(
            descriptor = CellDescriptor.EMPTY,
            bits = BitString.EMPTY,
            refs = emptyList(),
            hashes = arrayOf(
                "96a296d224f285c67bee93c30f8a309157f0daa35dc5b87e410b78630a09cfc7".hexToByteString()
            ),
            depths = intArrayOf(0)
        )
    }
}

public inline fun <T> DataCell.parse(block: CellSlice.() -> T): T {
    val slice = beginParse()
    return try {
        block(slice)
    } finally {
        slice.endParse()
    }
}