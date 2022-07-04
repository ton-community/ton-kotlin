package org.ton.cell

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellType.*
import org.ton.crypto.sha256
import kotlin.math.ceil
import kotlin.math.floor

@SerialName("data_cell")
@Serializable
class DataCell private constructor(
    override val bits: BitString,
    override val refs: List<Cell>,
    override val cellType: CellType
) : Cell {
    init {
        when (cellType) {
            ORDINARY -> Cell.checkRefsCount(refs.size)
            PRUNED_BRANCH -> Cell.checkRefsCount(0)
            LIBRARY_REFERENCE -> Cell.checkRefsCount(refs.size, 1..Cell.MAX_REFS)
            MERKLE_PROOF -> Cell.checkRefsCount(1)
            MERKLE_UPDATE -> Cell.checkRefsCount(2)
        }
    }

    private val hash by lazy {
        sha256(representation())
    }
    private val hashCode by lazy {
        hash.contentHashCode()
    }

    override val isExotic: Boolean = cellType.isExotic
    override val isMerkle: Boolean = cellType.isMerkle
    override val isPruned: Boolean = cellType.isPruned

    override val maxDepth: Int by lazy {
        refs.maxOfOrNull { it.maxDepth }?.plus(1) ?: 0
    }

    override val levelMask get() = LevelMask(0)

    override fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(refs)
        refs.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    override fun loadCell(): Cell =
        when(cellType) {
            ORDINARY -> this
            PRUNED_BRANCH -> error("Can't load pruned branch cell")
            LIBRARY_REFERENCE -> TODO()
            MERKLE_PROOF -> refs[0]
            MERKLE_UPDATE -> refs[1]
        }

    override fun beginParse(): CellSlice = CellSlice.beginParse(this)

    override fun descriptors(): ByteArray = byteArrayOf(referencesDescriptor(), bitsDescriptor())

    override fun hash(): ByteArray = hash

    override fun toString(): String = buildString {
        Cell.toString(cell = this@DataCell, appendable = this, firstChild = true, lastChild = true)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (other !is Cell) return false

        if (cellType != other.cellType) return false
        if (bits != other.bits) return false
        if (refs != other.refs) return false

        return true
    }

    override fun hashCode(): Int = hashCode

    private fun referencesDescriptor(): Byte =
        (refs.size + 8 * (if (isExotic) 1 else 0) + 32 * levelMask.level).toByte()

    private fun bitsDescriptor(): Byte =
        (ceil(bits.size / 8.0).toInt() + floor(bits.size / 8.0).toInt()).toByte()

    private fun augmentedBytes(): ByteArray =
        if (bits.size % 8 != 0) {
            BitString.appendAugmentTag(bits.toByteArray(), bits.size)
        } else bits.toByteArray()

    private fun representation(): ByteArray = buildPacket {
        writeFully(descriptors())
        writeFully(augmentedBytes())
        refs.forEach { reference ->
            writeShort(reference.maxDepth.toShort())
        }
        refs.forEach { reference ->
            val hash = reference.hash()
            writeFully(hash)
        }
    }.readBytes()

    companion object {
        @JvmStatic
        fun of(
            bits: BitString,
            refs: Iterable<Cell>,
            type: CellType = ORDINARY
        ): DataCell {
            val refsCollection = if (refs is List<Cell>) refs else refs.toList()
            Cell.checkRefsCount(refsCollection.size)
            return DataCell(bits, refsCollection, type)
        }

        fun of(
            bits: BitString,
            vararg refs: Cell
        ): DataCell {
            Cell.checkRefsCount(refs.size)
            return DataCell(bits, refs.toList(), ORDINARY)
        }
    }
}
