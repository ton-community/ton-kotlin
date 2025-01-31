@file:Suppress("NOTHING_TO_INLINE")

package org.ton.cell

import kotlinx.io.bytestring.ByteString
import org.ton.bigint.BigInt
import org.ton.bigint.toUInt
import org.ton.bigint.toULong
import org.ton.bitstring.BitString
import org.ton.bitstring.ByteBackedMutableBitString
import org.ton.bitstring.MutableBitString
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

    public constructor(slice: CellSlice, bitsCount: Int, refsCount: Int) {
        this.cell = slice.cell
        this.bitsEnd = slice.bitsStart + bitsCount
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

    public val refSize: Int get() = bitsStart - bitsEnd

    /**
     * Checks if slice is empty. If not, throws an exception.
     */
    public fun endParse() {

    }

    /**
     * Loads the first reference from the slice.
     */
    public fun loadRef(): Cell {
        check(refsPosition < refSize)
        return cell.refs[refsPosition++]
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

    public fun preloadBitString(bitCount: Int): BitString {
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
        val bits = ByteBackedMutableBitString(ByteArray(byteCount), byteCount * Byte.SIZE_BITS)
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
        bitsStart += byteCount
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
        if (bitCount == 0) return BigInt.ZERO
        val bytes = ByteArray((bitCount + 7) ushr 3)
        val bits = ByteBackedMutableBitString(bytes, bitCount)
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

    public fun preloadLong(bitCount: Int = Long.SIZE_BITS): Long = preloadBigInt(bitCount, signed = true).toLong()
    public fun loadLong(bitCount: Int = Long.SIZE_BITS): Long {
        val result = preloadLong(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadInt(bitCount: Int = Int.SIZE_BITS): Int = preloadBigInt(bitCount, signed = true).toInt()
    public fun loadInt(bitCount: Int = Int.SIZE_BITS): Int {
        val result = preloadInt(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadULong(bitCount: Int = ULong.SIZE_BITS): ULong = preloadBigInt(bitCount, signed = false).toULong()
    public fun loadULong(bitCount: Int = ULong.SIZE_BITS): ULong {
        val result = preloadULong(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadUInt(bitCount: Int = UInt.SIZE_BITS): UInt = preloadBigInt(bitCount, signed = false).toUInt()
    public fun loadUInt(bitCount: Int = UInt.SIZE_BITS): UInt {
        val result = preloadUInt(bitCount)
        bitsStart += bitCount
        return result
    }

    public fun preloadUIntLeq(max: Int): UInt = preloadUInt(Int.SIZE_BITS - max.countLeadingZeroBits())
    public fun loadUIntLeq(max: Int): UInt = loadUInt(Int.SIZE_BITS - max.countLeadingZeroBits())

    public fun preloadUIntLes(max: Int): UInt = loadUIntLeq(max - 1)
    public fun loadUIntLes(max: Int): UInt = loadUIntLeq(max - 1)

    public fun countLeadingBits(bit: Boolean): Int = cell.bits.countLeadingBits(bitsStart, bitsEnd, bit)
}
