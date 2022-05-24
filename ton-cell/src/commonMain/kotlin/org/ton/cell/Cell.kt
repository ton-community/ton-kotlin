package org.ton.cell

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.augment
import org.ton.crypto.sha256
import kotlin.math.ceil
import kotlin.math.floor

@Serializable
data class Cell(
        val bits: BitString,
        val references: List<Cell> = emptyList(),
        val type: CellType = CellType.ORDINARY
) {
    constructor(
            bits: String,
            vararg cellReferences: Cell,
            type: CellType = CellType.ORDINARY
    ) : this(
            BitString(bits),
            cellReferences.toList(),
            type
    )

    val isExotic: Boolean get() = type.isExotic

    val maxLevel: Int by lazy {
        // TODO: level calculation differ for exotic cells
        references.maxOfOrNull { it.maxLevel } ?: 0
    }

    val maxDepth: Int by lazy {
        val maxDepth = references.maxOf { it.maxDepth }
        if (references.isNotEmpty()) maxDepth + 1 else maxDepth
    }

    fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(references)
        references.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    fun descriptors(): ByteArray = byteArrayOf(referencesDescriptor(), bitsDescriptor())

    fun beginParse(): CellSlice = CellSlice.beginParse(this)

    fun <T : Any> parse(block: CellSlice.() -> T): T {
        val slice = beginParse()
        val result = block(slice)
        slice.endParse()
        return result
    }

    /**
     * Computes the representation hash of a cell and returns it as a 256-bit byte array.
     * Useful for signing and checking signatures of arbitrary entities represented by a tree of cells.
     */
    fun hash(): ByteArray = sha256(representation())

    override fun toString(): String = buildString {
        toString(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Cell

        if (bits != other.bits) return false
        if (references != other.references) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bits.hashCode()
        result = 31 * result + references.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    private fun referencesDescriptor(): Byte =
            (references.size + (if (isExotic) 1 else 0) * 8 + maxLevel * 32).toByte()

    private fun bitsDescriptor(): Byte =
            (ceil(bits.length / 8.0) + floor(bits.length / 8.0)).toInt().toByte()

    private fun augmentedBytes(): ByteArray =
            BitString(*bits.toBooleanArray().augment()).toByteArray()

    private fun representation(): ByteArray = buildPacket {
        writeFully(descriptors())
        writeFully(augmentedBytes())
        references.forEach { reference ->
            val depth = reference.maxDepth
            writeInt(depth)
        }
        references.forEach { reference ->
            val hash = reference.hash()
            writeFully(hash)
        }
    }.readBytes()

    private fun toString(appendable: Appendable, indent: String = "") {
        appendable.append(indent)
        appendable.append(bits.toString())
        appendable.append('\n')

        references.forEach { reference ->
            reference.toString(appendable, "$indent ")
        }
    }
}
