@file:Suppress("NOTHING_TO_INLINE")

package org.ton.kotlin.cell

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.bigint.BigInt
import org.ton.kotlin.bigint.toBigInt
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.ByteBackedBitString
import org.ton.kotlin.bitstring.ByteBackedMutableBitString
import org.ton.kotlin.bitstring.MutableBitString
import org.ton.kotlin.cell.exception.CellUnderflowException
import org.ton.kotlin.cell.serialization.CellLoader
import kotlin.experimental.inv

public class CellSlice {
    public val cell: DataCell

    internal var bitsStart: Int = 0
    internal val bitsEnd: Int

    internal var refsStart: Int = 0
    internal val refsEnd: Int

    public constructor(cell: DataCell) {
        this.cell = cell
        this.bitsEnd = cell.bits.size
        this.refsEnd = cell.refs.size
    }

    public constructor(slice: CellSlice, bitsCount: Int = 0, refsCount: Int = 0) {
        this.cell = slice.cell
        this.bitsStart = slice.bitsStart
        this.refsStart = slice.refsStart
        this.bitsEnd = slice.bitsEnd + bitsCount
        this.refsEnd = slice.refsEnd + refsCount
    }

    public var bitPosition: Int
        get() = bitsStart
        set(value) {
            require(value <= bitsEnd) { "bitsStart must be <= bitsEnd" }
            bitsStart = value
        }

    public var refsPosition: Int
        get() = refsStart
        set(value) {
            require(value <= refsEnd) { "refsStart must be <= refsEnd" }
            refsStart = value
        }

    public val size: Int get() = bitsEnd - bitsStart

    public val refSize: Int get() = refsEnd - refsStart

    public fun haveRefs(): Boolean = 1 <= refSize

    /**
     * Checks if slice is empty. If not, throws an exception.
     */
    public fun endParse() {

    }

    /**
     * Loads the first reference from the slice.
     */
    public fun loadRef(): Cell =
        loadRefOrNull() ?: throw CellUnderflowException("Out of range: $refsPosition in $refsStart..$refsEnd")

    public fun loadRefOrNull(): Cell? {
        return if (refsPosition < refSize) cell.refs[refsPosition++] else null
    }

    public fun preloadRef(offset: Int = 0): Cell {
        return cell.refs[refsPosition + offset]
    }

    public fun getBit(offset: Int): Int {
        return cell.bits.getBit(bitsStart + offset)
    }

    public fun loadBoolean(): Boolean {
        return cell.bits[bitsStart++]
    }

    public fun preloadBoolean(): Boolean {
        return cell.bits[bitsStart]
    }

    public fun skipBits(bitCount: Int): Boolean {
        if (bitCount > size) return false
        bitsStart += bitCount
        return true
    }

    public fun skipRefs(refCount: Int): Boolean {
        if (refCount > refSize) return false
        refsStart += refCount
        return true
    }

    public fun preloadBitString(bitCount: Int = size): BitString {
        require(bitCount <= size)
        val result = cell.bits.substring(bitsStart, bitsStart + bitCount)
        return result
    }

    public fun loadBitString(bitCount: Int): BitString {
        val result = preloadBitString(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadBitsTo(destination: MutableBitString, startIndex: Int = 0, endIndex: Int = size) {
        val bitCount = endIndex - startIndex
        val untilIndex = bitsStart + bitCount
        require(untilIndex <= size)
        cell.bits.copyInto(destination, startIndex, bitsStart, bitsStart + bitCount)
    }

    public fun loadBitsTo(destination: MutableBitString, startIndex: Int = 0, endIndex: Int = size) {
        preloadBitsTo(destination, startIndex, endIndex)
        bitsStart += (endIndex - startIndex)
    }

    public fun preloadBytesTo(destination: ByteArray, startIndex: Int = 0, endIndex: Int = size) {
        val byteCount = endIndex - startIndex
        val bits = ByteBackedMutableBitString(byteCount * Byte.SIZE_BITS)
        loadBitsTo(bits)
        bits.data.copyInto(destination, startIndex)
    }

    public fun loadBytesTo(destination: ByteArray, startIndex: Int = 0, endIndex: Int = size) {
        preloadBytesTo(destination, startIndex, endIndex)
        bitsStart += (endIndex - startIndex)
    }

    public fun preloadByteArray(byteCount: Int): ByteArray {
        require(byteCount <= size)
        val result = ByteArray(byteCount)
        preloadBytesTo(result)
        return result
    }

    public fun loadByteArray(byteCount: Int): ByteArray {
        val result = preloadByteArray(byteCount)
        bitsStart += byteCount * Byte.SIZE_BITS
        return result
    }

    public fun preloadByteString(byteCount: Int): ByteString = ByteString(*preloadByteArray(byteCount))
    public fun loadByteString(byteCount: Int): ByteString = ByteString(*loadByteArray(byteCount))

    public fun loadBigInt(bitCount: Int, signed: Boolean = true): BigInt {
        val result = preloadBigInt(bitCount, signed)
        bitsStart += bitCount
        return result
    }

    public fun preloadBigInt(bitCount: Int, signed: Boolean = true): BigInt {
        require(bitCount <= size)
        if (bitCount <= 0) return BigInt.ZERO
        if (bitCount <= 64) {
            return if (signed) preloadLong(bitCount).toBigInt() else preloadULong(bitCount).toBigInt()
        }
        val bits = ByteBackedMutableBitString(bitCount)
        val bytes = bits.data
        val shift = (bytes.size * Byte.SIZE_BITS) - bitCount
        return if (signed) {
            cell.bits.copyInto(bits, 0, bitsStart + 1, bitsStart + bitCount)
            val sign = if (getBit(0) != 0) {
                // is it faster than `val int = 1 shl (bitCount - 1); if (uint >= int) (uint - (int * 2)) uint`?
                bytes.forEachIndexed { index, byte ->
                    bytes[index] = byte.inv()
                }
                -1
            } else 1
            BigInt(bytes, sign).shr(shift + 1)
        } else {
            cell.bits.copyInto(bits, 0, bitsStart, bitsStart + bitCount)
            BigInt(bytes, 1).shr(shift)
        }
    }

    public fun preloadLong(bitCount: Int = Long.SIZE_BITS): Long {
        var offset = 0
        var data: ByteArray
        if (cell.bits is ByteBackedBitString) {
            data = cell.bits.data
            offset = bitPosition
        } else {
            data = ByteArray((bitCount + 7) ushr 3)
        }

        val r = offset and 7
        val q = offset ushr 3

        val firstByte = data[q].toInt() and (0xFF ushr r)
        val rightShift = (8 - (bitCount + r) % 8) % 8
        if (r + bitCount <= 8) {
            return (firstByte ushr rightShift).toLong()
        }
        val b = bitCount - 8 + r
        var result = 0L
        val byteCount = (b + 7) shr 3
        for (i in 1..byteCount) {
            val currentByte = data[q + i].toLong()
            result = (result shl 8) or (currentByte and 0xFF)
        }
        result = result ushr rightShift
        result = result or (firstByte.toLong() shl b)
        return result
    }

    public fun loadLong(bitCount: Int = Long.SIZE_BITS): Long {
        val result = preloadLong(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadInt(bitCount: Int = Int.SIZE_BITS): Int = preloadLong(bitCount).toInt()
    public fun loadInt(bitCount: Int = Int.SIZE_BITS): Int {
        val result = preloadInt(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadULong(bitCount: Int = ULong.SIZE_BITS): ULong = preloadLong(bitCount).toULong()
    public fun loadULong(bitCount: Int = ULong.SIZE_BITS): ULong {
        val result = preloadULong(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadUInt(bitCount: Int = UInt.SIZE_BITS): UInt = preloadULong(bitCount).toUInt()
    public fun loadUInt(bitCount: Int = UInt.SIZE_BITS): UInt {
        val result = preloadUInt(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadUIntLeq(max: Int): UInt = preloadUInt(Int.SIZE_BITS - max.countLeadingZeroBits())
    public fun loadUIntLeq(max: Int): UInt = loadUInt(Int.SIZE_BITS - max.countLeadingZeroBits())

    public fun preloadUIntLes(max: Int): UInt = loadUIntLeq(max - 1)
    public fun loadUIntLes(max: Int): UInt = loadUIntLeq(max - 1)

    public fun <T> load(loader: CellLoader<T>, context: CellContext = CellContext.EMPTY): T {
        return loader.load(this, context)
    }

    public fun <T> loadNullable(loader: CellLoader<T>, context: CellContext = CellContext.EMPTY): T? {
        return if (loadBoolean()) loader.load(this, context) else null
    }

    public fun countLeadingBits(bit: Boolean): Int = cell.bits.countLeadingBits(bitsStart, bitsEnd, bit)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CellSlice

        if (bitsStart != other.bitsStart) return false
        if (bitsEnd != other.bitsEnd) return false
        if (refsStart != other.refsStart) return false
        if (refsEnd != other.refsEnd) return false
        if (cell != other.cell) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bitsStart
        result = 31 * result + bitsEnd
        result = 31 * result + refsStart
        result = 31 * result + refsEnd
        result = 31 * result + cell.hashCode()
        return result
    }

    override fun toString(): String {
        return "CellSlice(bits=$bitsStart..$bitsEnd, refs=$refsStart..$refsEnd, data=${
            cell.bits.substring(
                bitsStart,
                bitsEnd
            )
        })"
    }
}
