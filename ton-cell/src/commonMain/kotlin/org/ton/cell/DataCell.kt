package org.ton.cell

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.crypto.sha256
import kotlin.math.ceil
import kotlin.math.floor

@Serializable
internal class DataCell private constructor(
    override val bits: BitString,
    override val refs: List<Cell>,
    override val type: CellType
) : Cell {
    init {
        Cell.checkRefsCount(refs.size)
    }

    override val isExotic: Boolean = type.isExotic
    override val isMerkle: Boolean = type.isMerkle
    override val isPruned: Boolean = type.isPruned

    override val maxLevel: Int by lazy {
        // TODO: level calculation differ for exotic cells
        refs.maxOfOrNull { it.maxLevel } ?: 0
    }

    override val maxDepth: Int by lazy {
        refs.maxOfOrNull { it.maxDepth }?.plus(1) ?: 0
    }

    override fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(refs)
        refs.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    override fun beginParse(): CellSlice = CellSlice.beginParse(this)

    override fun descriptors(): ByteArray = byteArrayOf(referencesDescriptor(), bitsDescriptor())

    override fun hash(): ByteArray = sha256(representation())

    override fun toString(): String = buildString {
        Cell.toString(cell = this@DataCell, appendable = this, firstChild = true, lastChild = true)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DataCell

        if (bits != other.bits) return false
        if (refs != other.refs) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bits.hashCode()
        result = 31 * result + refs.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    private fun referencesDescriptor(): Byte =
        (refs.size + (if (isExotic) 1 else 0) * 8 + maxLevel * 32).toByte()

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
            type: CellType = CellType.ORDINARY
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
            return DataCell(bits, refs.toList(), CellType.ORDINARY)
        }
    }
}
