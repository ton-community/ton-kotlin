package org.ton.cell

import org.ton.primitives.BigInt
import kotlin.jvm.JvmStatic


interface CellBuilder {

    /**
     * @return the number of cell references already stored in builder.
     */
    val builderRefs: Int

    /**
     * @return the number of data bits already stored in builder.
     */
    val builderBits: Int

    /**
     * @return the depth of builder. If no cell references are stored in builder, then returns `0`;
     * otherwise the returned value is one plus the maximum of depths of cells referred to from builder.
     */
    val builderDepth: Int

    /**
     * Converts a builder into an ordinary cell.
     */
    fun endCell(): Cell

    /**
     * Stores a reference to cell into builder.
     */
    fun storeRef(cell: Cell): CellBuilder

    /**
     * Stores an unsigned [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 256.
     */
    fun storeUInt(value: BigInt, length: Int): CellBuilder
    fun storeUInt(value: Long, length: Int): CellBuilder = storeUInt(BigInt(value), length)
    fun storeUInt(value: Int, length: Int): CellBuilder = storeUInt(BigInt(value), length)
    fun storeUInt(value: Short, length: Int): CellBuilder = storeUInt(BigInt(value), length)
    fun storeUInt(value: Byte, length: Int): CellBuilder = storeUInt(BigInt(value), length)

    /**
     * Stores a signed [length]-bit integer [value] into builder for 0 ≤ [length] ≤ 257.
     */
    fun storeInt(value: BigInt, length: Int): CellBuilder
    fun storeInt(value: Long, length: Int): CellBuilder = storeInt(BigInt(value), length)
    fun storeInt(value: Int, length: Int): CellBuilder = storeInt(BigInt(value), length)
    fun storeInt(value: Short, length: Int): CellBuilder = storeInt(BigInt(value), length)
    fun storeInt(value: Byte, length: Int): CellBuilder = storeInt(BigInt(value), length)

    /**
     * Stores [slice] into builder.
     */
    fun storeSlice(slice: CellSlice): CellBuilder

    /**
     * Stores (serializes) an integer [value] in the range `0..2^128 − 1` into builder.
     * The serialization of [value] consists of a 4-bit unsigned big-endian integer `l`,
     * which is the smallest integer `l ≥ 0`, such that `x < 2^8*l`,
     * followed by an `8*l`-bit unsigned big-endian representation of [value].
     * If [value] does not belong to the supported range, a range check exception is thrown.
     */
    fun storeGrams(value: BigInt): CellBuilder
    fun storeGrams(value: Long): CellBuilder = storeGrams(BigInt(value))
    fun storeGrams(value: Int): CellBuilder = storeGrams(BigInt(value))
    fun storeGrams(value: Short): CellBuilder = storeGrams(BigInt(value))
    fun storeGrams(value: Byte): CellBuilder = storeGrams(BigInt(value))

    /**
     * Stores dictionary represented by cell [cell] or null into builder.
     * In other words, stores a `1`-bit and a reference to [cell]. If [cell] is not null and `0`-bit otherwise.
     */
    fun storeDict(cell: Cell?): CellBuilder

    companion object {
        @JvmStatic
        fun beginCell(): CellBuilder = CellBuilderImpl()
    }
}

private class CellBuilderImpl(

) : CellBuilder {
    override val builderRefs: Int
        get() = TODO("Not yet implemented")
    override val builderBits: Int
        get() = TODO("Not yet implemented")
    override val builderDepth: Int
        get() = TODO("Not yet implemented")

    override fun endCell(): Cell {
        TODO("Not yet implemented")
    }

    override fun storeRef(cell: Cell): CellBuilder {
        TODO("Not yet implemented")
    }

    override fun storeUInt(value: BigInt, length: Int): CellBuilder {
        TODO("Not yet implemented")
    }

    override fun storeInt(value: BigInt, length: Int): CellBuilder {
        TODO("Not yet implemented")
    }

    override fun storeSlice(slice: CellSlice): CellBuilder {
        TODO("Not yet implemented")
    }

    override fun storeGrams(value: BigInt): CellBuilder {
        TODO("Not yet implemented")
    }

    override fun storeDict(cell: Cell?): CellBuilder {
        TODO("Not yet implemented")
    }
}

//private class CellBuilderImpl(
//    private val data: BitStringBuilder = BitStringBuilder(),
//    override var cellReferences: MutableList<Cell> = ArrayList(),
//) : CellBuilder, BitStringBuilder by data {
//    override var writePosition: Int
//        get() = data.writePosition
//        set(value) {
//            data.writePosition = value
//        }
//
//    override fun toCell() = Cell(data.build(), cellReferences)
//}
//
//@JsName("createCellBuilder")
//fun CellBuilder(): CellBuilder = CellBuilderImpl()
//
//fun buildCell(builder: CellBuilder.() -> Unit): Cell = CellBuilder().apply(builder).toCell()