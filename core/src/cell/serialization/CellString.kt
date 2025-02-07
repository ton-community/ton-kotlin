package org.ton.kotlin.cell.serialization

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.ByteBackedMutableBitString
import org.ton.kotlin.cell.Cell
import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import kotlin.math.min

public object CellString : CellSerializer<ByteArray> {
    public const val MAX_SIZE_BYTES: Int = 1024
    public const val MAX_SIZE_BITS: Int = MAX_SIZE_BYTES * Byte.SIZE_BITS
    public const val MAX_CHAIN_LENGTH: Int = 16

    public fun byteArray(): CellSerializer<ByteArray> = this

    public fun byteString(): CellSerializer<ByteString> = object : CellSerializer<ByteString> {
        override fun load(
            slice: CellSlice,
            context: CellContext
        ): ByteString = ByteString(*this@CellString.load(slice, context))

        override fun store(
            builder: CellBuilder,
            value: ByteString,
            context: CellContext
        ) = this@CellString.store(builder, value.toByteArray(), context)
    }

    public fun string(): CellSerializer<String> = object : CellSerializer<String> {
        override fun load(
            slice: CellSlice,
            context: CellContext
        ): String = this@CellString.load(slice, context).decodeToString()

        override fun store(
            builder: CellBuilder,
            value: String,
            context: CellContext
        ) = this@CellString.store(builder, value.encodeToByteArray(), context)
    }

    override fun load(
        slice: CellSlice,
        context: CellContext
    ): ByteArray {
        val segments = ArrayList<BitString>(MAX_CHAIN_LENGTH)
        var head = min(slice.size, MAX_SIZE_BITS)
        var nextSlice = slice
        while (true) {
            val segment = nextSlice.loadBitString(head)
            segments.add(segment)
            nextSlice = context.loadCell(nextSlice.loadRefOrNull() ?: break).asCellSlice()
            head = nextSlice.size
        }

        val size = segments.sumOf { it.size }
        require(size % Byte.SIZE_BITS == 0) {
            "CellString size is not divisible by 8: $size"
        }
        val result = ByteArray(size / 8)
        val bitString = ByteBackedMutableBitString(result, size)
        var offset = 0
        segments.forEach { segment ->
            bitString.copyInto(bitString, offset)
            offset += segment.size
        }
        return result
    }

    override fun store(
        builder: CellBuilder,
        value: ByteArray,
        context: CellContext
    ) {
        var currentFrom = 0
        var remainingSize = value.size

        require(remainingSize <= MAX_SIZE_BITS) { "CellString bits is too long: $remainingSize" }

        val maxBits = Cell.MAX_SIZE_BITS / 8 * 8
        val depth = 1 + (remainingSize + maxBits - 1) / maxBits
        require(depth <= MAX_CHAIN_LENGTH) { "CellString depth is too deep: $depth" }

        var currentBuilder = builder

        val bitString = BitString(value)
        while (remainingSize > 0) {
            val head = min(remainingSize, min(currentBuilder.remainingBits, Cell.MAX_SIZE_BITS))
            currentBuilder.storeBitString(bitString, currentFrom, currentFrom + head)

            currentFrom += head
            remainingSize -= head

            if (remainingSize > 0) {
                val childBuilder = CellBuilder()
                currentBuilder.storeRef(context.finalizeCell(childBuilder))
                currentBuilder = childBuilder
            }
        }
    }
}