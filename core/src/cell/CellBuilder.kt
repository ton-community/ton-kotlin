package org.ton.kotlin.cell

import io.github.andreypfau.kotlinx.crypto.Sha256
import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.bigint.BigInt
import org.ton.kotlin.bigint.toBigInt
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.ByteBackedMutableBitString
import org.ton.kotlin.bitstring.bitsCopy
import org.ton.kotlin.bitstring.bitsStoreLong
import org.ton.kotlin.cell.exception.CellOverflowException
import org.ton.kotlin.cell.serialization.CellStorer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.jvm.JvmStatic
import kotlin.math.max

public inline operator fun CellBuilder.invoke(builder: CellBuilder.() -> Unit) {
    builder(this)
}

@OptIn(ExperimentalContracts::class)
public inline fun buildCell(builderAction: CellBuilder.() -> Unit): DataCell {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    return CellBuilder.beginCell().apply(builderAction).endCell()
}

public inline fun CellBuilder.storeRef(refBuilder: CellBuilder.() -> Unit): CellBuilder = apply {
    val cellBuilder = CellBuilder.beginCell()
    cellBuilder.apply(refBuilder)
    val cell = cellBuilder.endCell()
    storeRef(cell)
}


public class CellBuilder private constructor(
    private var bits: ByteBackedMutableBitString = ByteBackedMutableBitString(1023),
    private val refs: Array<Cell?> = Array(4) { null },
    public var levelMask: LevelMask? = null,
    public var isExotic: Boolean = false,
    private val hasher: Sha256
) {
    public constructor() : this(hasher = CELL_BUILDER_HASHER)

    public var bitsPosition: Int = 0
    private var refPosition: Int = 0
    public val remainingBits: Int get() = Cell.MAX_SIZE_BITS - bitsPosition

    public fun storeBoolean(bit: Boolean): CellBuilder {
        checkBitsOverflow(1)
        bits[bitsPosition] = bit
        bitsPosition++
        return this
    }

    public fun storeBits(vararg value: Boolean): CellBuilder {
        checkBitsOverflow(value.size)
        this.bits.setBitsAt(bitsPosition, value.asIterable())
        bitsPosition += value.size
        return this
    }

    public fun storeBits(value: Collection<Boolean>): CellBuilder {
        checkBitsOverflow(value.size)
        this.bits.setBitsAt(bitsPosition, value)
        bitsPosition += value.size
        return this
    }

    public fun storeBits(value: BitString, bits: Int = value.size): CellBuilder {
        return storeBitString(value, 0, bits)
    }

    public fun storeBits(value: ByteArray, bits: Int): CellBuilder {
        checkBitsOverflow(bits)
        this.bits.setBitsAt(bitsPosition, value, bits)
        bitsPosition += bits
        return this
    }

    public fun storeBitString(
        value: BitString,
        startIndex: Int = 0,
        endIndex: Int = value.size
    ): CellBuilder {
        val length = endIndex - startIndex
        checkBitsOverflow(length)
        value.copyInto(bits, bitsPosition, startIndex, endIndex)
        bitsPosition += length
        return this
    }

    public fun storeByteArray(byteArray: ByteArray): CellBuilder {
        val bitCount = byteArray.size * Byte.SIZE_BITS
        checkBitsOverflow(bitCount)
        this.bits.setBitsAt(bitsPosition, byteArray, bitCount)
        bitsPosition += bitCount
        return this
    }

    public fun storeByteString(byteString: ByteString): CellBuilder {
        val bitLen = byteString.size * Byte.SIZE_BITS
        checkBitsOverflow(bitLen)
        this.bits.setBitsAt(bitsPosition, byteString, bitLen)
        bitsPosition += bitLen
        return this
    }

    public fun storeByteArray(byteArray: ByteArray, length: Int): CellBuilder {
        checkBitsOverflow(length)
        this.bits.setBitsAt(bitsPosition, byteArray, length)
        bitsPosition += length
        return this
    }

    public fun storeRef(ref: Cell): CellBuilder {
        if (refPosition >= 4) {
            throw CellOverflowException("Refs overflow. Can't add refs. ${4 - refPosition} refs left.")
        }
        refs[refPosition++] = ref
        return this
    }

    public fun storeBigInt(
        value: BigInt,
        bitCount: Int,
        signed: Boolean
    ): CellBuilder {
        if (value == BigInt.ZERO) {
            checkBitsOverflow(bitCount)
            bitsPosition += bitCount
            return this
        }
        if (bitCount < 64) {
            checkBitsOverflow(bitCount)
            bitsStoreLong(bits.data, bitsPosition, value.toLong(), bitCount)
            bitsPosition += bitCount
            return this
        }
        val bytes = value.toByteArray()
        val actualBitLen = bytes.size * Byte.SIZE_BITS
        if (signed) {
            this.bits[bitsPosition] = value.sign < 0
        }
        bitsCopy(
            this.bits.data,
            bitsPosition + bitCount - value.bitLength,
            bytes,
            actualBitLen - value.bitLength,
            value.bitLength
        )
        bitsPosition += bitCount
        return this
    }

    public fun storeInt(value: Int, bitCount: Int = Int.SIZE_BITS): CellBuilder {
        if (bitCount > Long.SIZE_BITS) {
            return storeBigInt(value.toBigInt(), bitCount, true)
        }
        checkBitsOverflow(bitCount)
        bitsStoreLong(bits.data, bitsPosition, value.toLong(), bitCount)
        bitsPosition += bitCount
        return this
    }

    public fun storeLong(value: Long, bitCount: Int = Long.SIZE_BITS): CellBuilder {
        if (bitCount > Long.SIZE_BITS) {
            return storeBigInt(value.toBigInt(), bitCount, true)
        }
        checkBitsOverflow(bitCount)
        bitsStoreLong(bits.data, bitsPosition, value, bitCount)
        bitsPosition += bitCount
        return this
    }

    public fun storeUInt(value: UInt, bitCount: Int = UInt.SIZE_BITS): CellBuilder {
        if (bitCount > Long.SIZE_BITS) {
            return storeBigInt(value.toBigInt(), bitCount, false)
        }
        checkBitsOverflow(bitCount)
        bitsStoreLong(bits.data, bitsPosition, value.toLong(), bitCount)
        bitsPosition += bitCount
        return this
    }

    public fun storeULong(value: ULong, bitCount: Int = ULong.SIZE_BITS): CellBuilder {
        if (bitCount > Long.SIZE_BITS) {
            return storeBigInt(value.toBigInt(), bitCount, false)
        }
        checkBitsOverflow(bitCount)
        bitsStoreLong(bits.data, bitsPosition, value.toLong(), bitCount)
        bitsPosition += bitCount
        return this
    }

    public fun storeVarUInt(value: Long, maxByteCount: Int): CellBuilder {
        return storeVarUInt(value.toBigInt(), maxByteCount)
    }

    public fun storeVarUInt(value: BigInt, maxByteCount: Int): CellBuilder {
        val byteCount = (value.bitLength + 7) ushr 3
        storeIntLess(byteCount, maxByteCount)
        val bitCount = byteCount * Byte.SIZE_BITS
        return storeBigInt(value, bitCount, false)
    }

    public fun storeIntLess(value: Int, upperBound: Int): CellBuilder {
        require(value < upperBound)
        return storeInt(value, Int.SIZE_BITS - (upperBound - 1).countLeadingZeroBits())
    }

    public fun storeUIntLess(value: UInt, upperBound: UInt): CellBuilder {
        require(value < upperBound)
        return storeUInt(value, UInt.SIZE_BITS - (upperBound - 1u).countLeadingZeroBits())
    }

    public fun storeIntLeq(value: Int, upperBound: Int): CellBuilder {
        require(value <= upperBound)
        return storeInt(value, Int.SIZE_BITS - upperBound.countLeadingZeroBits())
    }

    public fun storeUIntLeq(value: UInt, upperBound: UInt): CellBuilder {
        require(value <= upperBound)
        return storeUInt(value, UInt.SIZE_BITS - upperBound.countLeadingZeroBits())
    }

    public fun storeSlice(slice: CellSlice): CellBuilder {
        val cell = slice.cell
        val bits = cell.bits
        val refs = cell.refs

        checkRefsOverflow(refs.size)
        storeBitString(bits, slice.bitsStart, slice.bitsEnd)
        for (i in slice.refsStart until slice.refsEnd) {
            this.refs[refPosition++] = refs[i]
        }
        return this
    }

    public fun <T> store(
        serializer: CellStorer<T>,
        value: T,
        context: CellContext = CellContext.EMPTY
    ): CellBuilder {
        serializer.store(this, value, context)
        return this
    }

    public fun <T> storeNullable(
        serializer: CellStorer<T>,
        value: T?,
        context: CellContext = CellContext.EMPTY
    ): CellBuilder {
        if (value == null) {
            storeBoolean(false)
        } else {
            storeBoolean(true)
            serializer.store(this, value, context)
        }
        return this
    }

    public fun toBitString(): BitString = bits.substring(0, bitsPosition)

    public fun toCellSlice(): CellSlice = build().asCellSlice()

    public fun reset(): CellBuilder {
        bits.data.fill(0)
        bitsPosition = 0
        refs.fill(null)
        refPosition = 0
        return this
    }

    public fun endCell(): DataCell = build()

    public fun build(): DataCell {
        var childrenMask = LevelMask()
        val refs = ArrayList<Cell>(refPosition)
        for (i in 0 until refPosition) {
            val child = this.refs[i] ?: continue
            childrenMask = childrenMask or child.levelMask
            refs.add(child)
        }

        val levelMask = levelMask ?: childrenMask
        val d1 = CellDescriptor.computeD1(levelMask, isExotic, refs.size)
        val d2 = CellDescriptor.computeD2(bitsPosition)
        val descriptor = CellDescriptor(d1, d2)
        if (descriptor == CellDescriptor.EMPTY) {
            return DataCell.EMPTY
        }

        val bits = toBitString()
        val data = bits.toByteArray(augment = true)
        val levels = descriptor.levelMask.level + 1
        val hashes = Array(levels) { ByteArray(32) }
        val depths = IntArray(levels)
        computeHashes(descriptor, data, childrenMask, refs, hashes, depths)

        return DataCell(descriptor, bits, refs, Array(levels) { ByteString(*hashes[it]) }, depths)
    }

    private fun computeHashes(
        descriptor: CellDescriptor,
        data: ByteArray,
        childrenMask: LevelMask,
        refs: List<Cell>,
        hashes: Array<ByteArray>,
        depths: IntArray
    ) {
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

        var (d1, d2) = descriptor
        val hasher = hasher
        val refCount = refs.size
        val buf = ByteArray(max(2, (32 + 2) * refCount))
        repeat(levels) { level ->
            hasher.reset()
            val levelMask = if (descriptor.cellType == CellType.PRUNED_BRANCH) {
                descriptor.levelMask
            } else {
                LevelMask.level(level)
            }
            d1 = d1 and (CellDescriptor.LEVEL_MASK or CellDescriptor.HAS_HASHES_MASK).inv().toByte()
            d1 = d1 or (levelMask.mask shl 5).toByte()
            buf[0] = d1
            buf[1] = d2
            hasher.update(buf, 0, 2)

            if (level == 0) {
                hasher.update(data)
            } else {
                val prevHash = hashes[level - 1]
                hasher.update(prevHash)
            }

            var depth = 0
            val hashOffset = 2 * refCount
            repeat(refCount) { refIndex ->
                val child = refs[refIndex]
                val childDepth = child.depth(level + levelOffset)
                depth = max(depth, childDepth + 1)

                val depthOffset = 2 * refIndex
                buf[depthOffset] = (childDepth ushr Byte.SIZE_BITS).toByte()
                buf[depthOffset + 1] = childDepth.toByte()
                child.hash(level + levelOffset).copyInto(buf, hashOffset + 32 * refIndex)
            }
            if (refCount > 0) {
                hasher.update(buf)
            }

            depths[level] = depth
            hasher.digest(hashes[level])
        }
    }

    override fun toString(): String = endCell().toString()

    private fun checkBitsOverflow(length: Int) = require(length <= remainingBits) {
        throw CellOverflowException("Bits overflow. Can't add $length bits. $remainingBits bits left. - ${bits.size}")
    }

    private fun checkRefsOverflow(count: Int) = require(count < (4 - refPosition)) {
        throw CellOverflowException("Refs overflow. Can't add $count refs. ${4 - refPosition} refs left.")
    }

    public companion object {
        public const val HASH_BITS: Int = 256
        public const val DEPTH_BITS: Int = 16

        @JvmStatic
        public fun beginCell(): CellBuilder = CellBuilder()

        @OptIn(ExperimentalContracts::class)
        @JvmStatic
        public fun createCell(builder: CellBuilder.() -> Unit): DataCell {
            contract {
                callsInPlace(builder, InvocationKind.EXACTLY_ONCE)
            }
            val cellBuilder = CellBuilder()
            builder(cellBuilder)
            return cellBuilder.build()
        }

        @JvmStatic
        public fun createPrunedBranch(cell: Cell, merkleDepth: Int): Cell = buildCell {
            val levelMask = LevelMask.level(cell.levelMask.mask or (1 shl merkleDepth)).also {
                levelMask = it
            }
            isExotic = true
            storeInt(CellType.PRUNED_BRANCH.value, 8)
            storeInt(levelMask.mask, 8)

            val hashCount = cell.levelMask.hashCount
            repeat(hashCount) { level ->
                storeByteString(cell.hash(level))
            }
            repeat(hashCount) { level ->
                storeInt(cell.depth(level), 16)
            }
        }
    }
}
