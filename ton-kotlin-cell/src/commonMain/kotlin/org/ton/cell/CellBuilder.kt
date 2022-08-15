package org.ton.cell

import org.ton.bigint.*
import org.ton.bitstring.BitString
import org.ton.bitstring.ByteBackedMutableBitString
import org.ton.bitstring.MutableBitString
import org.ton.cell.exception.CellOverflowException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface CellBuilder {
    var bits: MutableBitString
    var refs: MutableList<Cell>

    val bitsPosition: Int

    /**
     * Converts a builder into an ordinary cell.
     */
    fun endCell(): Cell

    fun storeBit(bit: Boolean): CellBuilder
    fun storeBits(vararg bits: Boolean): CellBuilder
    fun storeBits(bits: Iterable<Boolean>): CellBuilder
    fun storeBits(bits: Collection<Boolean>): CellBuilder

    fun storeBytes(byteArray: ByteArray): CellBuilder

    /**
     * Stores a reference to cell into builder.
     */
    fun storeRef(ref: Cell): CellBuilder

    fun storeRefs(vararg refs: Cell): CellBuilder
    fun storeRefs(refs: Iterable<Cell>): CellBuilder
    fun storeRefs(refs: Collection<Cell>): CellBuilder

    /**
     * Stores an unsigned [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 256.
     */
    fun storeUInt(value: BigInt, length: Int): CellBuilder
    fun storeUInt(value: Byte, length: Int): CellBuilder = storeUInt(BigInt(value), length)
    fun storeUInt(value: Short, length: Int): CellBuilder = storeUInt(BigInt(value), length)
    fun storeUInt(value: Int, length: Int): CellBuilder = storeUInt(BigInt(value), length)
    fun storeUInt(value: Long, length: Int): CellBuilder = storeUInt(BigInt(value), length)

    fun storeUInt32(value: UInt) = storeInt(value.toInt(), 32)
    fun storeUInt64(value: ULong) = storeInt(value.toLong(), 64)

    fun storeUIntLeq(value: BigInt, max: BigInt): CellBuilder = storeUInt(value, max.bitLength)
    fun storeUIntLeq(value: Byte, max: Byte): CellBuilder = storeUIntLeq(BigInt(value), BigInt(max))
    fun storeUIntLeq(value: Short, max: Short): CellBuilder = storeUIntLeq(BigInt(value), BigInt(max))
    fun storeUIntLeq(value: Int, max: Int): CellBuilder = storeUIntLeq(BigInt(value), BigInt(max))
    fun storeUIntLeq(value: Long, max: Long): CellBuilder = storeUIntLeq(BigInt(value), BigInt(max))

    fun storeUIntLes(value: BigInt, max: BigInt): CellBuilder = storeUInt(value, (max - 1).bitLength)
    fun storeUIntLes(value: Byte, max: Byte): CellBuilder = storeUIntLes(BigInt(value), BigInt(max))
    fun storeUIntLes(value: Short, max: Short): CellBuilder = storeUIntLes(BigInt(value), BigInt(max))
    fun storeUIntLes(value: Int, max: Int): CellBuilder = storeUIntLes(BigInt(value), BigInt(max))
    fun storeUIntLes(value: Long, max: Long): CellBuilder = storeUIntLes(BigInt(value), BigInt(max))

    /**
     * Stores a signed [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 257.
     */
    fun storeInt(value: BigInt, length: Int): CellBuilder
    fun storeInt(value: Byte, length: Int): CellBuilder = storeInt(BigInt(value), length)
    fun storeInt(value: Short, length: Int): CellBuilder = storeInt(BigInt(value), length)
    fun storeInt(value: Int, length: Int): CellBuilder = storeInt(BigInt(value), length)
    fun storeInt(value: Long, length: Int): CellBuilder = storeInt(BigInt(value), length)

    /**
     * Stores [slice] into builder.
     */
    fun storeSlice(slice: CellSlice): CellBuilder

    companion object {
        @JvmStatic
        fun of(cell: Cell): CellBuilder =
            CellBuilderImpl(BitString.MAX_LENGTH, cell.bits.toMutableBitString(), cell.refs.toMutableList())

        @JvmStatic
        fun beginCell(maxLength: Int = BitString.MAX_LENGTH): CellBuilder = CellBuilderImpl(maxLength)

        @JvmStatic
        fun createCell(maxLength: Int = BitString.MAX_LENGTH, builder: CellBuilder.() -> Unit): Cell =
            CellBuilderImpl(maxLength).apply(builder).endCell()

        @JvmStatic
        fun createPrunedBranch(cell: Cell, newLevel: Int, virtualizationLevel: Int = Cell.MAX_LEVEL): Cell =
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
        fun createMerkleProof(cellProof: Cell): Cell = createCell {
            storeUInt(CellType.MERKLE_PROOF.value, 8)
            storeBytes(cellProof.hash(level = 0))
            storeUInt(cellProof.depth(level = 0), Cell.DEPTH_BITS)
            storeRef(cellProof)
        }

        @JvmStatic
        fun createMerkleUpdate(fromProof: Cell, toProof: Cell) = createCell {
            storeUInt(CellType.MERKLE_UPDATE.value, 8)
            storeBytes(fromProof.hash(level = 0))
            storeBytes(toProof.hash(level = 0))
            storeUInt(fromProof.depth(level = 0), Cell.DEPTH_BITS)
            storeUInt(toProof.depth(level = 0), Cell.DEPTH_BITS)
            storeRef(fromProof)
            storeRef(toProof)
        }
    }

    fun storeBytes(byteArray: ByteArray, length: Int): CellBuilder
}

inline operator fun CellBuilder.invoke(builder: CellBuilder.() -> Unit) {
    builder(this)
}

inline fun CellBuilder.storeRef(refBuilder: CellBuilder.() -> Unit): CellBuilder = apply {
    val cellBuilder = CellBuilder.beginCell()
    cellBuilder.apply(refBuilder)
    val cell = cellBuilder.endCell()
    storeRef(cell)
}

fun CellBuilder(cell: Cell): CellBuilder =
    CellBuilder.of(cell)

@OptIn(ExperimentalContracts::class)
fun CellBuilder(maxLength: Int = BitString.MAX_LENGTH, builder: CellBuilder.() -> Unit = {}): CellBuilder {
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
        val intBits = BigInt(1) shl (length - 1)
        require(value >= -intBits && value < intBits) { "Can't store an Int, because its value allocates more space than provided." }
        storeNumber(value, length)
    }

    private fun storeNumber(value: BigInt, length: Int): CellBuilder = apply {
        val bits = BooleanArray(length) { index ->
            ((value shr index) and BigInt(1)).toInt() == 1
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
