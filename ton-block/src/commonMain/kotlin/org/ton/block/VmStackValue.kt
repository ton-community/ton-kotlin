@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice

fun VmStackValue(): VmStackValue = VmStackValue.of()
fun VmStackValue(byte: Byte): VmStackValue.TinyInt = VmStackValue.of(byte)
fun VmStackValue(short: Short): VmStackValue.TinyInt = VmStackValue.of(short)
fun VmStackValue(int: Int): VmStackValue.TinyInt = VmStackValue.of(int)
fun VmStackValue(long: Long): VmStackValue.TinyInt = VmStackValue.of(long)
fun VmStackValue(bigInt: BigInt): VmStackValue.Int = VmStackValue.of(bigInt)
fun VmStackValue(cell: Cell): VmStackValue.Cell = VmStackValue.of(cell)
fun VmStackValue(cellSlice: CellSlice): VmStackValue.Slice = VmStackValue.of(cellSlice)
fun VmStackValue(cellBuilder: CellBuilder): VmStackValue.Builder = VmStackValue.of(cellBuilder)
fun VmStackValue(cont: VmCont): VmStackValue.Cont = VmStackValue.of(cont)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmStackValue {
    @SerialName("vm_stk_null")
    @Serializable
    object Null : VmStackValue

    @SerialName("vm_stk_tinyint")
    @Serializable
    data class TinyInt(
        val value: Long
    ) : VmStackValue {
        constructor(byte: Byte) : this(byte.toLong())
        constructor(short: Short) : this(short.toLong())
        constructor(int: kotlin.Int) : this(int.toLong())
    }

    @SerialName("vm_stk_int")
    @Serializable
    data class Int(
        @Serializable(BigIntSerializer::class)
        val value: BigInt
    ) : VmStackValue {
        constructor(byte: Byte) : this(BigInt(byte))
        constructor(short: Short) : this(BigInt(short))
        constructor(int: kotlin.Int) : this(BigInt(int))
        constructor(long: Long) : this(BigInt(long))
    }

    @SerialName("vm_stk_nan")
    object Nan : VmStackValue

    @SerialName("vm_stk_cell")
    data class Cell(
        val cell: org.ton.cell.Cell
    ) : VmStackValue

    @SerialName("vm_stk_slice")
    data class Slice(
        @SerialName("_")
        val slice: VmCellSlice
    ) : VmStackValue {
        constructor(cellSlice: CellSlice) : this(VmCellSlice(cellSlice))

        fun toCellSlice(): CellSlice = slice.toCellSlice()
    }

    @SerialName("vm_stk_builder")
    data class Builder(
        val cell: org.ton.cell.Cell
    ) : VmStackValue {
        constructor(cellBuilder: CellBuilder) : this(cellBuilder.endCell())

        fun toCellBuilder(): CellBuilder = CellBuilder(cell)
    }

    @SerialName("vm_stk_cont")
    data class Cont(
        val cont: VmCont
    ) : VmStackValue

    @SerialName("vm_stk_tuple")
    data class Tuple(
        val len: kotlin.Int,
        val data: VmTuple
    ) : VmStackValue

    companion object {
        @JvmStatic
        fun of(): VmStackValue = Null

        @JvmStatic
        fun of(byte: Byte): TinyInt = TinyInt(byte)

        @JvmStatic
        fun of(short: Short): TinyInt = TinyInt(short)

        @JvmStatic
        fun of(int: kotlin.Int): TinyInt = TinyInt(int)

        @JvmStatic
        fun of(long: Long): TinyInt = TinyInt(long)

        @JvmStatic
        fun of(bigInt: BigInt): Int = Int(bigInt)

        @JvmStatic
        fun of(cell: org.ton.cell.Cell): Cell = Cell(cell)

        @JvmStatic
        fun of(cellSlice: CellSlice): Slice = Slice(cellSlice)

        @JvmStatic
        fun of(cellBuilder: CellBuilder): Builder = Builder(cellBuilder)

        @JvmStatic
        fun of(cont: VmCont): Cont = Cont(cont)
    }
}

