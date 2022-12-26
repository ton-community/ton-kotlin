package org.ton.cell

import org.ton.bigint.*
import org.ton.bitstring.BitString
import org.ton.bitstring.ByteBackedMutableBitString
import org.ton.bitstring.MutableBitString
import org.ton.cell.exception.CellOverflowException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

public interface CellBuilder {
    public var bits: MutableBitString
    public var refs: MutableList<Cell>

    public val bitsPosition: Int

    /**
     * Converts a builder into an ordinary cell.
     */
    public fun endCell(): Cell

    public fun storeBit(bit: Boolean): CellBuilder
    public fun storeBits(vararg bits: Boolean): CellBuilder
    public fun storeBits(bits: Iterable<Boolean>): CellBuilder
    public fun storeBits(bits: Collection<Boolean>): CellBuilder

    public fun storeBytes(byteArray: ByteArray): CellBuilder

    /**
     * Stores a reference to cell into builder.
     */
    public fun storeRef(ref: Cell): CellBuilder

    public fun storeRefs(vararg refs: Cell): CellBuilder
    public fun storeRefs(refs: Iterable<Cell>): CellBuilder
    public fun storeRefs(refs: Collection<Cell>): CellBuilder

    /**
     * Stores an unsigned [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 256.
     */
    public fun storeUInt(value: BigInt, length: Int): CellBuilder
    public fun storeUInt(value: Byte, length: Int): CellBuilder = storeUInt(value.toLong(), length)
    public fun storeUInt(value: Short, length: Int): CellBuilder = storeUInt(value.toLong(), length)
    public fun storeUInt(value: Int, length: Int): CellBuilder = storeUInt(value.toLong(), length)
    public fun storeUInt(value: Long, length: Int): CellBuilder = storeUInt(value.toBigInt(), length)

    public fun storeUInt8(value: UByte): CellBuilder = storeInt(value.toByte(), 8)
    public fun storeUInt16(value: UShort): CellBuilder = storeInt(value.toShort(), 16)
    public fun storeUInt32(value: UInt): CellBuilder = storeInt(value.toInt(), 32)
    public fun storeUInt64(value: ULong): CellBuilder = storeInt(value.toLong(), 64)

    public fun storeUIntLeq(value: BigInt, max: BigInt): CellBuilder = storeUInt(value, max.bitLength)
    public fun storeUIntLeq(value: Byte, max: Byte): CellBuilder = storeUIntLeq(value.toBigInt(), max.toBigInt())
    public fun storeUIntLeq(value: Short, max: Short): CellBuilder = storeUIntLeq(value.toBigInt(), max.toBigInt())
    public fun storeUIntLeq(value: Int, max: Int): CellBuilder = storeUIntLeq(value.toBigInt(), max.toBigInt())
    public fun storeUIntLeq(value: Long, max: Long): CellBuilder = storeUIntLeq(value.toBigInt(), max.toBigInt())

    public fun storeUIntLes(value: BigInt, max: BigInt): CellBuilder = storeUInt(value, (max - 1).bitLength)
    public fun storeUIntLes(value: Byte, max: Byte): CellBuilder = storeUIntLes(value.toBigInt(), max.toBigInt())
    public fun storeUIntLes(value: Short, max: Short): CellBuilder = storeUIntLes(value.toBigInt(), max.toBigInt())
    public fun storeUIntLes(value: Int, max: Int): CellBuilder = storeUIntLes(value.toBigInt(), max.toBigInt())
    public fun storeUIntLes(value: Long, max: Long): CellBuilder = storeUIntLes(value.toBigInt(), max.toBigInt())

    /**
     * Stores a signed [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 257.
     */
    public fun storeInt(value: BigInt, length: Int): CellBuilder
    public fun storeInt(value: Byte, length: Int): CellBuilder = storeInt(value.toBigInt(), length)
    public fun storeInt(value: Short, length: Int): CellBuilder = storeInt(value.toBigInt(), length)
    public fun storeInt(value: Int, length: Int): CellBuilder = storeInt(value.toBigInt(), length)
    public fun storeInt(value: Long, length: Int): CellBuilder = storeInt(value.toBigInt(), length)

    /**
     * Stores [slice] into builder.
     */
    public fun storeSlice(slice: CellSlice): CellBuilder

    public companion object {
        @JvmStatic
        public fun of(cell: Cell): CellBuilder =
            CellBuilderImpl(BitString.MAX_LENGTH, cell.bits.toMutableBitString(), cell.refs.toMutableList())

        @JvmStatic
        public fun beginCell(maxLength: Int = BitString.MAX_LENGTH): CellBuilder = CellBuilderImpl(maxLength)

        @JvmStatic
        public fun createCell(maxLength: Int = BitString.MAX_LENGTH, builder: CellBuilder.() -> Unit): Cell =
            CellBuilderImpl(maxLength).apply(builder).endCell()

        @JvmStatic
        public fun createPrunedBranch(cell: Cell, newLevel: Int, virtualizationLevel: Int = Cell.MAX_LEVEL): Cell =
            createCell {
                val levelMask = cell.levelMask.apply(virtualizationLevel)
                val level = levelMask.level
                check(newLevel >= level + 1)

                storeUInt(CellType.PRUNED_BRANCH.value, 8)
                storeUInt((levelMask or LevelMask.level(newLevel)).mask, 8)
                repeat(level + 1) {
                    if (levelMask.isSignificant(it)) {
                        storeBytes(cell.hash(it))
                    }
                }
                repeat(level + 1) {
                    if (levelMask.isSignificant(it)) {
                        storeUInt(cell.depth(it), 16)
                    }
                }
            }

        @JvmStatic
        public fun createMerkleProof(cellProof: Cell): Cell = createCell {
            storeUInt(CellType.MERKLE_PROOF.value, 8)
            storeBytes(cellProof.hash(level = 0))
            storeUInt(cellProof.depth(level = 0), Cell.DEPTH_BITS)
            storeRef(cellProof)
        }

        @JvmStatic
        public fun createMerkleUpdate(fromProof: Cell, toProof: Cell): Cell = createCell {
            storeUInt(CellType.MERKLE_UPDATE.value, 8)
            storeBytes(fromProof.hash(level = 0))
            storeBytes(toProof.hash(level = 0))
            storeUInt(fromProof.depth(level = 0), Cell.DEPTH_BITS)
            storeUInt(toProof.depth(level = 0), Cell.DEPTH_BITS)
            storeRef(fromProof)
            storeRef(toProof)
        }
    }

    public fun storeBytes(byteArray: ByteArray, length: Int): CellBuilder
}

public inline fun CellBuilder.storeUInt(value: UByte, bits: Int): CellBuilder = storeUInt(value.toLong(), bits)
public inline fun CellBuilder.storeUInt(value: UShort, bits: Int): CellBuilder = storeUInt(value.toLong(), bits)
public inline fun CellBuilder.storeUInt(value: UInt, bits: Int): CellBuilder = storeUInt(value.toLong(), bits)
public inline fun CellBuilder.storeUInt(value: ULong, bits: Int): CellBuilder =
    storeUInt(BigInt(value.toString()), bits)

public inline operator fun CellBuilder.invoke(builder: CellBuilder.() -> Unit) {
    builder(this)
}

public inline fun CellBuilder.storeRef(refBuilder: CellBuilder.() -> Unit): CellBuilder = apply {
    val cellBuilder = CellBuilder.beginCell()
    cellBuilder.apply(refBuilder)
    val cell = cellBuilder.endCell()
    storeRef(cell)
}

public fun CellBuilder(cell: Cell): CellBuilder =
    CellBuilder.of(cell)

@OptIn(ExperimentalContracts::class)
public fun CellBuilder(maxLength: Int = BitString.MAX_LENGTH, builder: CellBuilder.() -> Unit = {}): CellBuilder {
    contract {
        callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
    }
    return CellBuilderImpl(maxLength).apply(builder)
}

private class CellBuilderImpl(
    val maxLength: Int,
    override var bits: MutableBitString = ByteBackedMutableBitString.of(),
    override var refs: MutableList<Cell> = ArrayList()
) : CellBuilder {
    private val remainder: Int get() = maxLength - bitsPosition
    override val bitsPosition: Int get() = bits.size

    override fun endCell(): Cell = Cell(bits, refs)

    override fun storeBit(bit: Boolean): CellBuilder = apply {
        checkBitsOverflow(1)
        bits += bit
    }

    override fun storeBits(vararg bits: Boolean): CellBuilder = apply {
        checkBitsOverflow(bits.size)
        this.bits += bits
    }

    override fun storeBits(bits: Collection<Boolean>): CellBuilder = apply {
        checkBitsOverflow(bits.size)
        this.bits.plus(bits)
    }

    override fun storeBits(bits: Iterable<Boolean>): CellBuilder = apply {
        val currentSize = this.bits.size
        this.bits.plus(bits)
        checkBitsOverflow(this.bits.size - currentSize)
    }

    override fun storeBytes(byteArray: ByteArray): CellBuilder = apply {
        checkBitsOverflow(byteArray.size * Byte.SIZE_BITS)
        this.bits.plus(byteArray)
    }

    override fun storeBytes(byteArray: ByteArray, length: Int): CellBuilder = apply {
        checkBitsOverflow(length)
        this.bits.plus(byteArray, length)
    }

    override fun storeRef(ref: Cell): CellBuilder = apply {
        checkRefsOverflow(1)
        refs.add(ref)
    }

    override fun storeRefs(vararg refs: Cell): CellBuilder = apply {
        checkRefsOverflow(refs.size)
        this.refs.addAll(refs.filter { it.bits.isNotEmpty() })
    }

    override fun storeRefs(refs: Iterable<Cell>): CellBuilder = storeRefs(refs.toList())

    override fun storeRefs(refs: Collection<Cell>): CellBuilder = apply {
        checkRefsOverflow(refs.size)
        this.refs.addAll(refs.filter { it.bits.isNotEmpty() })
    }

    override fun storeUInt(value: BigInt, length: Int): CellBuilder = apply {
        check(value.bitLength <= length) { "Integer `$value` does not fit into $length bits" }
        check(value.sign >= 0) { "Integer `$value` must be unsigned" }
        storeNumber(value, length)
    }

    override fun storeInt(value: BigInt, length: Int): CellBuilder = apply {
        val intBits = 1.toBigInt() shl (length - 1)
        require(value >= -intBits && value < intBits) { "Can't store an Int, because its value allocates more space than provided." }
        storeNumber(value, length)
    }

    private fun storeNumber(value: BigInt, length: Int): CellBuilder = apply {
        val bits = BooleanArray(length) { index ->
            ((value shr index) and 1.toBigInt()).toInt() == 1
        }.reversedArray()
        storeBits(*bits)
    }

    override fun storeSlice(slice: CellSlice): CellBuilder = apply {
        val (bits, refs) = slice

        checkBitsOverflow(bits.size)
        checkRefsOverflow(refs.size)

        storeBits(bits)
        refs.forEach { ref ->
            storeRef(ref)
        }
    }

    override fun toString(): String = endCell().toString()

    private fun checkBitsOverflow(length: Int) = require(length <= remainder) {
        throw CellOverflowException("Bits overflow. Can't add $length bits. $remainder bits left.")
    }

    private fun checkRefsOverflow(count: Int) = require(count <= (4 - refs.size)) {
        throw CellOverflowException("Refs overflow. Can't add $count refs. ${4 - refs.size} refs left.")
    }
}
