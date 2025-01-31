package org.ton.cell

import kotlinx.io.bytestring.ByteString
import kotlinx.io.bytestring.toHexString
import org.ton.bitstring.BitString

public class PrunedBranchCell(
    private val hash: ByteString,
    private val depth: Int,
    public val descriptor: CellDescriptor,
    public val bits: BitString
) : Cell {
    override val levelMask: LevelMask = descriptor.levelMask
    private var hashCode: Int = 0

    override fun hash(level: Int): ByteString {
        val hashIndex = descriptor.levelMask.apply(level).hashIndex
        return if (hashIndex == descriptor.levelMask.level) {
            hash
        } else {
            val offset = 2 + hashIndex * 32
            ByteString(*bits.toByteArray().copyOfRange(offset, offset + 32))
        }
    }

    override fun depth(level: Int): Int {
        val hashIndex = descriptor.levelMask.apply(level).hashIndex
        return if (hashIndex == descriptor.levelMask.level) {
            depth
        } else {
            val offset = 2 + descriptor.levelMask.level * 32 + hashIndex * 2
            val data = bits.toByteArray()
            ((data[offset].toInt() and 0xFF) shl 8) or (data[offset + 1].toInt() and 0xFF)
        }
    }

    override fun treeWalk(): Sequence<Cell> = emptySequence()

    override fun virtualize(offset: Int): Cell =
        VirtualCell(this, offset)

    override fun toString(): String = "Cell(${CellType.PRUNED_BRANCH}, ${hash().toHexString()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrunedBranchCell) return false
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
}
