@file:Suppress("OPT_IN_USAGE")

package org.ton.cell

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import kotlin.jvm.JvmStatic

public inline fun Cell(hex: String, vararg refs: Cell, isExotic: Boolean = false): Cell =
    Cell.of(BitString(hex), refs = refs.toList(), isExotic)

public inline fun Cell(hex: String, refs: Iterable<Cell> = emptyList(), isExotic: Boolean = false): Cell =
    Cell.of(BitString(hex), refs, isExotic)

public inline fun Cell(
    bits: BitString = BitString.empty(),
    refs: Iterable<Cell> = emptyList(),
    isExotic: Boolean = false
): Cell =
    Cell.of(bits, refs, isExotic)

public inline fun Cell(bits: BitString, vararg refs: Cell, isExotic: Boolean = false): Cell =
    Cell.of(bits, refs = refs, isExotic)

@JsonClassDiscriminator("@type")
public interface Cell {
    public val bits: BitString
    public val refs: List<Cell>
    public val type: CellType

    public val isExotic: Boolean get() = type.isExotic
    public val isMerkle: Boolean get() = type.isMerkle
    public val isPruned: Boolean get() = type.isPruned
    public val levelMask: LevelMask

    public fun isEmpty(): Boolean = bits.isEmpty() && refs.isEmpty()

    public fun hash(level: Int = MAX_LEVEL): ByteArray
    public fun depth(level: Int = MAX_LEVEL): Int

    public fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(refs)
        refs.forEach { reference ->
            yieldAll(reference.treeWalk())
        }
    }

    public fun loadCell(): Cell =
        when (type) {
            CellType.ORDINARY -> this
            CellType.PRUNED_BRANCH -> error("Can't load pruned branch cell")
            CellType.LIBRARY_REFERENCE -> TODO()
            CellType.MERKLE_PROOF -> refs[0]
            CellType.MERKLE_UPDATE -> refs[1]
        }

    public fun beginParse(): CellSlice = CellSlice.beginParse(this)

    public fun <T> parse(block: CellSlice.() -> T): T {
        val slice = beginParse()
        val result = block(slice)
        slice.endParse()
        return result
    }

    public fun toPrunedBranch(newLevel: Int, virtualizationLevel: Int = MAX_LEVEL): Cell =
        CellBuilder.createPrunedBranch(this, newLevel, virtualizationLevel)

    public fun toMerkleProof(): Cell =
        CellBuilder.createMerkleProof(this)

    public fun toMerkleUpdate(toProof: Cell): Cell =
        CellBuilder.createMerkleUpdate(this, toProof)

    public fun descriptors(): ByteArray = byteArrayOf(getRefsDescriptor(), getBitsDescriptor())
    public fun getBitsDescriptor(): Byte = Companion.getBitsDescriptor(bits)
    public fun getRefsDescriptor(): Byte = Companion.getRefsDescriptor(refs.size, isExotic, levelMask)

    override fun toString(): String

    public companion object {
        public const val HASH_BYTES: Int = 32
        public const val HASH_BITS: Int = HASH_BYTES * Byte.SIZE_BITS
        public const val DEPTH_BYTES: Int = 2
        public const val DEPTH_BITS: Int = DEPTH_BYTES * Byte.SIZE_BITS
        public const val MAX_LEVEL: Int = 3
        public const val MAX_DEPTH: Int = 1024

        @JvmStatic
        public fun of(hex: String, vararg refs: Cell, isExotic: Boolean = false): Cell =
            CellImpl.of(BitString(hex), refs.toList(), isExotic)

        @JvmStatic
        public fun of(
            bits: BitString = BitString(),
            refs: Iterable<Cell> = emptyList(),
            isExotic: Boolean = false
        ): Cell = CellImpl.of(bits, refs.toList(), isExotic)

        @JvmStatic
        public fun of(
            bits: BitString,
            vararg refs: Cell,
            isExotic: Boolean = false
        ): Cell = CellImpl.of(bits, refs.toList(), isExotic)

        @JvmStatic
        public fun toString(cell: Cell): String = buildString {
            toString(cell, this)
        }

        @JvmStatic
        public fun toString(
            cell: Cell,
            appendable: Appendable,
            indent: String = ""
        ) {
            appendable.append(indent)
            if (cell.isExotic) {
                appendable.append(cell.type.toString())
                appendable.append(' ')
            }
            appendable.append("x{")
            appendable.append(cell.bits.toString())
            appendable.append("}")
            cell.refs.forEach { reference ->
                appendable.append('\n')
                toString(reference, appendable, "$indent    ")
            }
        }

        @JvmStatic
        public fun getRefsDescriptor(refs: Int, isExotic: Boolean, levelMask: LevelMask): Byte {
            return (refs + ((if (isExotic) 1 else 0) * 8) + (levelMask.mask * 32)).toByte()
        }

        @JvmStatic
        public fun getBitsDescriptor(bits: BitString): Byte {
            val result = (bits.size / 8) * 2
            if ((bits.size and 7) != 0) {
                return (result + 1).toByte()
            }
            return result.toByte()
        }
    }
}
