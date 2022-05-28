package org.ton.boc

import io.ktor.utils.io.core.*
import org.ton.cell.Cell
import org.ton.crypto.hex

internal data class BagOfCellsImpl(
    override val roots: List<Cell>,
    override val isIndexed: Boolean = false,
    override val crc32hash: ByteArray? = null
) : BagOfCells, List<Cell> by roots {
    constructor(root: Cell) : this(roots = listOf(root))

    constructor(
        root: Cell,
        isIndexed: Boolean,
        crc32hash: ByteArray?
    ) : this(listOf(root), isIndexed, crc32hash)

    override fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(roots)
        roots.forEach { root ->
            yieldAll(root.treeWalk())
        }
    }.distinct()

    override fun toByteArray(): ByteArray = buildPacket {
        writeBagOfCells(this@BagOfCellsImpl)
    }.readBytes()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BagOfCells

        if (roots != other.roots) return false
        if (isIndexed != other.isIndexed) return false
        if (crc32hash != null) {
            if (other.crc32hash == null) return false
            if (!crc32hash.contentEquals(other.crc32hash)) return false
        } else if (other.crc32hash != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roots.hashCode()
        result = 31 * result + isIndexed.hashCode()
        result = 31 * result + (crc32hash?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String = buildString {
        append("BagOfCells(roots=")
        append(roots)
        append(", isIndexed=")
        append(isIndexed)
        append(", crc32hash=")
        append(crc32hash?.let { hex(it) })
        append(")")
        roots.forEachIndexed { index, cell ->
            val firstChild = index == 0
            val lastChild = index == roots.lastIndex
            Cell.toString(cell, this, "", firstChild, lastChild)
        }
    }
}
