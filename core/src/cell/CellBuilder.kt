package org.ton.cell

import io.github.andreypfau.kotlinx.crypto.Sha256
import kotlinx.io.bytestring.ByteString
import org.ton.bigint.BigInt
import org.ton.bigint.toBigInt
import org.ton.bitstring.*
import org.ton.cell.exception.CellOverflowException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.jvm.JvmStatic
import kotlin.math.max

public interface CellBuilder {
    public var levelMask: LevelMask?
    public var isExotic: Boolean

    public val bitsPosition: Int
    public val remainingBits: Int

    /**
     * Converts a builder into an ordinary cell.
     */
    public fun endCell(): Cell = build()
    public fun build(): Cell

    public fun storeBit(value: Boolean): CellBuilder
    public fun storeBits(vararg value: Boolean): CellBuilder
    public fun storeBits(value: Collection<Boolean>): CellBuilder
    public fun storeBits(value: BitString): CellBuilder
    public fun storeBits(value: ByteArray, bits: Int): CellBuilder

    public fun storeByteArray(byteArray: ByteArray): CellBuilder
    public fun storeByteString(byteString: ByteString): CellBuilder
    public fun storeByte(byte: Byte): CellBuilder

    /**
     * Stores a reference to cell into builder.
     */
    public fun storeRef(ref: Cell): CellBuilder

    public fun storeRefs(vararg refs: Cell): CellBuilder
    public fun storeRefs(refs: Iterable<Cell>): CellBuilder
    public fun storeRefs(refs: Collection<Cell>): CellBuilder

    /**
     * Stores an unsigned [bitLength]-bit integer [value] into builder for 0 ≤ [bitLength] ≤ 256.
     */
    public fun storeUInt(value: BigInt, bitLength: Int): CellBuilder
    public fun storeUInt(value: Byte, bitLength: Int): CellBuilder = storeUInt(value.toInt(), bitLength)
    public fun storeUInt(value: Short, bitLength: Int): CellBuilder = storeUInt(value.toInt(), bitLength)
    public fun storeUInt(value: Int, bitLength: Int): CellBuilder = storeUInt(value.toBigInt(), bitLength)
    public fun storeUInt(value: Long, bitLength: Int): CellBuilder = storeUInt(value.toBigInt(), bitLength)

    public fun storeUInt8(value: UByte): CellBuilder = storeInt(value.toByte(), 8)
    public fun storeUInt16(value: UShort): CellBuilder = storeInt(value.toShort(), 16)
    public fun storeUInt32(value: UInt): CellBuilder = storeInt(value.toInt(), 32)
    public fun storeUInt64(value: ULong): CellBuilder = storeInt(value.toLong(), 64)

    public fun storeUIntLeq(value: BigInt, max: BigInt): CellBuilder = storeUInt(value, max.bitLength)
    public fun storeUIntLeq(value: Byte, max: Byte): CellBuilder = storeUIntLeq(value.toInt(), max.toInt())
    public fun storeUIntLeq(value: Short, max: Short): CellBuilder = storeUIntLeq(value.toInt(), max.toInt())
    public fun storeUIntLeq(value: Int, max: Int): CellBuilder = storeUIntLeq(value.toBigInt(), max.toBigInt())
    public fun storeUIntLeq(value: Long, max: Long): CellBuilder =
        storeUIntLeq(value.toBigInt(), max.toBigInt())

    public fun storeUIntLes(value: BigInt, max: BigInt): CellBuilder =
        storeUInt(value, (max - BigInt.ONE).bitLength)

    public fun storeUIntLes(value: Byte, max: Byte): CellBuilder = storeUIntLes(value.toInt(), max.toInt())
    public fun storeUIntLes(value: Short, max: Short): CellBuilder = storeUIntLes(value.toInt(), max.toInt())
    public fun storeUIntLes(value: Int, max: Int): CellBuilder = storeUIntLes(value.toBigInt(), max.toBigInt())
    public fun storeUIntLes(value: Long, max: Long): CellBuilder =
        storeUIntLes(value.toBigInt(), max.toBigInt())

    public fun storeVarUInt(value: Long, maxByteLength: Int): CellBuilder
    public fun storeVarUInt(value: BigInt, maxByteLength: Int): CellBuilder

    /**
     * Stores a signed [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 257.
     */
    public fun storeInt(value: BigInt, length: Int): CellBuilder
    public fun storeInt(value: Byte, length: Int): CellBuilder = storeInt(value.toInt(), length)
    public fun storeInt(value: Short, length: Int): CellBuilder = storeInt(value.toInt(), length)
    public fun storeInt(value: Int, length: Int): CellBuilder = storeInt(value.toBigInt(), length)
    public fun storeInt(value: Long, length: Int): CellBuilder = storeInt(value.toBigInt(), length)

    /**
     * Stores [slice] into builder.
     */
    public fun storeSlice(slice: CellSlice): CellBuilder

    public fun toBitString(): BitString

    public companion object {
        @JvmStatic
        public fun of(cell: Cell): CellBuilder =
            CellBuilderImpl(cell.bits.toMutableBitString(), cell.refs.toMutableList())

        @JvmStatic
        public fun beginCell(): CellBuilder = CellBuilderImpl()

        @OptIn(ExperimentalContracts::class)
        @JvmStatic
        public fun createCell(builder: CellBuilder.() -> Unit): Cell {
            contract {
                callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
            }
            val cellBuilder = CellBuilderImpl()
            builder(cellBuilder)
            return cellBuilder.build()
        }

        @JvmStatic
        public fun createPrunedBranch(cell: Cell, merkleDepth: Int): Cell = buildCell {
            val descriptor = cell.descriptor
            val levelMask = LevelMask.level(descriptor.levelMask.mask or (1 shl merkleDepth)).also {
                levelMask = it
            }
            isExotic = true
            storeByte(CellType.PRUNED_BRANCH.value.toByte())
            storeByte(levelMask.mask.toByte())

            val hashCount = descriptor.hashCount
            repeat(hashCount) { level ->
                storeBits(cell.hash(level))
            }
            repeat(hashCount) { level ->
                storeUInt16(cell.depth(level).toUShort())
            }
        }
    }

    public fun storeByteArray(byteArray: ByteArray, length: Int): CellBuilder
}

public inline operator fun CellBuilder.invoke(builder: CellBuilder.() -> Unit) {
    builder(this)
}

@OptIn(ExperimentalContracts::class)
public inline fun buildCell(builderAction: CellBuilder.() -> Unit): Cell {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return CellBuilder.beginCell().apply(builderAction).endCell()
}

public inline fun CellBuilder.storeRef(refBuilder: CellBuilder.() -> Unit): CellBuilder = apply {
    val cellBuilder = CellBuilder.beginCell()
    cellBuilder.apply(refBuilder)
    val cell = cellBuilder.endCell()
    storeRef(cell)
}

@Suppress("NOTHING_TO_INLINE")
public inline fun CellBuilder(cell: Cell): CellBuilder = CellBuilder.of(cell)

@Suppress("NOTHING_TO_INLINE")
public inline fun CellBuilder(): CellBuilder = CellBuilder.beginCell()

private class CellBuilderImpl(
    var bits: MutableBitString = ByteBackedMutableBitString(ByteArray(128), 1023),
    var refs: MutableList<Cell> = ArrayList(),
    override var levelMask: LevelMask? = null,
    override var isExotic: Boolean = false
) : CellBuilder {
    override var bitsPosition: Int = 0
    override val remainingBits: Int get() = Cell.MAX_BITS_SIZE - bitsPosition

    override fun storeBit(bit: Boolean): CellBuilder = apply {
        checkBitsOverflow(1)
        bits[bitsPosition] = bit
        bitsPosition++
    }

    override fun storeBits(vararg value: Boolean): CellBuilder = apply {
        checkBitsOverflow(value.size)
        this.bits.setBitsAt(bitsPosition, value.asIterable())
        bitsPosition += value.size
    }

    override fun storeBits(value: Collection<Boolean>): CellBuilder = apply {
        this.bits.setBitsAt(bitsPosition, value)
        bitsPosition += value.size
    }

    override fun storeBits(bits: BitString): CellBuilder = apply {
        checkBitsOverflow(bits.size)
        this.bits.setBitsAt(bitsPosition, bits)
        bitsPosition += bits.size
    }

    override fun storeBits(value: ByteArray, bits: Int): CellBuilder = apply {
        checkBitsOverflow(bits)
        this.bits.setBitsAt(bitsPosition, value, bits)
        bitsPosition += bits
    }

    override fun storeByteArray(byteArray: ByteArray): CellBuilder = apply {
        val bitLen = byteArray.size * Byte.SIZE_BITS
        checkBitsOverflow(bitLen)
        this.bits.setBitsAt(bitsPosition, byteArray, bitLen)
        bitsPosition += bitLen
    }

    override fun storeByteString(byteString: ByteString): CellBuilder = apply {
        val bitLen = byteString.size * Byte.SIZE_BITS
        checkBitsOverflow(bitLen)
        this.bits.setBitsAt(bitsPosition, byteString, bitLen)
        bitsPosition += bitLen
    }

    override fun storeByte(byte: Byte): CellBuilder = apply {
        checkBitsOverflow(Byte.SIZE_BITS)
        this.bits.setBitsAt(bitsPosition, byteArrayOf(byte), Byte.SIZE_BITS)
        bitsPosition += Byte.SIZE_BITS
    }

    override fun storeByteArray(byteArray: ByteArray, length: Int): CellBuilder = apply {
        checkBitsOverflow(length)
        this.bits.setBitsAt(bitsPosition, byteArray, length)
        bitsPosition += length
    }

    override fun storeRef(ref: Cell): CellBuilder = apply {
        checkRefsOverflow(1)
        refs.add(ref)
    }

    override fun storeRefs(vararg refs: Cell): CellBuilder = apply {
        checkRefsOverflow(refs.size)
        this.refs.addAll(refs)
    }

    override fun storeRefs(refs: Iterable<Cell>): CellBuilder = storeRefs(refs.toList())

    override fun storeRefs(refs: Collection<Cell>): CellBuilder = apply {
        checkRefsOverflow(refs.size)
        this.refs.addAll(refs)
    }

    override fun storeUInt(value: BigInt, bitLength: Int): CellBuilder = apply {
        bits.setUBigIntAt(bitsPosition, value, bitLength)
        bitsPosition += bitLength
    }

    override fun storeVarUInt(value: Long, maxByteLength: Int): CellBuilder = apply {
        storeVarUInt(value.toBigInt(), maxByteLength)
    }

    override fun storeVarUInt(value: BigInt, maxByteLength: Int): CellBuilder = apply {
        val bytes = (value.bitLength + Byte.SIZE_BITS - 1) / Byte.SIZE_BITS
        storeUIntLes(bytes, 16)
        val bits = bytes * Byte.SIZE_BITS
        storeUInt(value, bits)
    }

    override fun storeInt(value: BigInt, length: Int): CellBuilder = apply {
        val intBits = 1.toBigInt() shl (length - 1)
        require(value >= -intBits && value < intBits) { "Can't store an Int, because its value allocates more space than provided." }
        bits.setBigIntAt(bitsPosition, value, length)
        bitsPosition += length
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

    override fun toBitString(): BitString {
        val bytes = bits.toByteArray()
        return ByteBackedBitString.of(bytes, bitsPosition)
    }

    override fun build(): Cell {
        var childrenMask = LevelMask()
        refs.forEach { child ->
            childrenMask = childrenMask or child.levelMask
        }

        val levelMask = levelMask ?: childrenMask
        val d1 = CellDescriptor.computeD1(levelMask, isExotic, refs.size)
        val d2 = CellDescriptor.computeD2(bitsPosition)
        val descriptor = CellDescriptor(d1, d2)

        val bits = toBitString()
        val data = bits.toByteArray(augment = true)
        val hashes = computeHashes(descriptor, data, childrenMask)

        return when (descriptor.cellType) {
            CellType.PRUNED_BRANCH -> {
                check(hashes.size == 1)
                val (hash, depth) = hashes[0]
                PrunedBranchCell(
                    hash.toBitString(), depth, descriptor, bits
                )
            }

            else -> if (descriptor == EmptyCell.descriptor) {
                EmptyCell
            } else {
                DataCell(descriptor, bits, refs, hashes)
            }
        }
    }

    private fun computeHashes(
        descriptor: CellDescriptor,
        data: ByteArray,
        childrenMask: LevelMask
    ): List<Pair<ByteArray, Int>> {
        var levels = descriptor.levelMask.level + 1

        val computedLevelMask = when (descriptor.cellType) {
            CellType.ORDINARY -> childrenMask
            // 8 bits type, 8 bits level mask, level x (hash, depth)
            CellType.PRUNED_BRANCH -> {
                check(data[0].toInt() == CellType.PRUNED_BRANCH.value) {
                    "Cell type mismatch, expected: ${CellType.PRUNED_BRANCH} ${CellType.PRUNED_BRANCH.value}, actual: ${data[0]}"
                }
                val expectedBitLength = 8 + 8 + descriptor.levelMask.level * (HASH_BITS + DEPTH_BITS)
                check(bitsPosition == expectedBitLength) {
                    "Invalid bit length, expected: $expectedBitLength, actual: $bitsPosition"
                }
                check(refs.isEmpty()) {
                    "Pruned branch contains non empty references"
                }
                val storedMask = data[1].toInt()
                check(descriptor.levelMask.mask == storedMask) {
                    "Invalid level mask in pruned branch, expected: ${descriptor.levelMask.mask}, actual: $storedMask"
                }
                levels = 1
                descriptor.levelMask
            }
            // 8 bits type, hash, depth
            CellType.MERKLE_PROOF -> {
                check(data[0].toInt() == CellType.MERKLE_PROOF.value) {
                    "Cell type mismatch, expected: ${CellType.MERKLE_PROOF} ${CellType.MERKLE_PROOF.value}, actual: ${data[0]}"
                }
                val expectedBitLength = 8 + HASH_BITS + DEPTH_BITS
                check(bitsPosition == expectedBitLength) {
                    "Invalid bit length, expected: $expectedBitLength, actual: $bitsPosition"
                }
                check(refs.size == 1) {
                    "Invalid merkle proof reference count, expected: 1, actual: ${refs.size}"
                }
                childrenMask.virtualize(1)
            }
            // 8 bits type, 2 x (hash, depth)
            CellType.MERKLE_UPDATE -> {
                check(data[0].toInt() == CellType.MERKLE_UPDATE.value) {
                    "Cell type mismatch, expected: ${CellType.MERKLE_UPDATE} ${CellType.MERKLE_UPDATE.value}, actual: ${data[0]}"
                }
                val expectedBitLength = 8 + 2 * (HASH_BITS + DEPTH_BITS)
                check(bitsPosition == expectedBitLength) {
                    println(bits)
                    "Invalid bit length, expected: $expectedBitLength, actual: $bitsPosition"
                }
                check(refs.size == 2) {
                    "Invalid merkle update reference count, expected: 2, actual: ${refs.size}"
                }
                childrenMask.virtualize(1)
            }
            // 8 bits type, hash
            CellType.LIBRARY_REFERENCE -> {
                check(data[0].toInt() == CellType.LIBRARY_REFERENCE.value) {
                    "Cell type mismatch, expected: ${CellType.LIBRARY_REFERENCE} ${CellType.LIBRARY_REFERENCE.value}, actual: ${data[0]}"
                }
                val expectedBitLength = 8 + HASH_BITS
                check(bitsPosition == expectedBitLength) {
                    "Invalid bit length, expected: $expectedBitLength, actual: $bitsPosition"
                }
                check(refs.isEmpty()) {
                    "Invalid library reference count, expected: 0, actual: ${refs.size}"
                }
                LevelMask()
            }
        }

        check(descriptor.levelMask == computedLevelMask) {
            "Invalid level mask, expected: $levelMask, actual: $computedLevelMask"
        }

        val levelOffset = if (descriptor.cellType.isMerkle) 1 else 0
        val hashes = ArrayList<Pair<ByteArray, Int>>(levels)

        var (d1, d2) = descriptor
        val hasher = Sha256()
        repeat(levels) { level ->
            hasher.reset()
            val levelMask = if (descriptor.cellType == CellType.PRUNED_BRANCH) {
                descriptor.levelMask
            } else {
                LevelMask.level(level)
            }
            d1 = d1 and (CellDescriptor.LEVEL_MASK or CellDescriptor.HAS_HASHES_MASK).inv().toByte()
            d1 = d1 or (levelMask.mask shl 5).toByte()
            hasher.update(d1)
            hasher.update(d2)

            if (level == 0) {
                hasher.update(data)
            } else {
                val prevHash = hashes[level - 1].first
                hasher.update(prevHash)
            }

            var depth = 0
            refs.forEach { child ->
                val childDepth = child.depth(level + levelOffset)
                depth = max(depth, childDepth + 1)

                hasher.update((childDepth ushr Byte.SIZE_BITS).toByte())
                hasher.update(childDepth.toByte())
            }

            refs.forEach { child ->
                val childHash = child.hash(level + levelOffset).toByteArray()
                hasher.update(childHash)
            }

            val hash = hasher.digest()
            hashes.add(hash to depth)
        }

        return hashes
    }

    override fun toString(): String = endCell().toString()

    private fun checkBitsOverflow(length: Int) = require(length <= remainingBits) {
        throw CellOverflowException("Bits overflow. Can't add $length bits. $remainingBits bits left. - ${bits.size}")
    }

    private fun checkRefsOverflow(count: Int) = require(count <= (4 - refs.size)) {
        throw CellOverflowException("Refs overflow. Can't add $count refs. ${4 - refs.size} refs left.")
    }

    companion object {
        const val HASH_BITS = 256
        const val DEPTH_BITS = 16
    }
}
