@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bigint.BigInt
import org.ton.bigint.BigIntSerializer
import org.ton.cell.Cell
import org.ton.cell.*
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

inline fun VmStackValue(): VmStackValue = VmStackValue.of()
inline fun VmStackValue(byte: Byte): VmStackValue.TinyInt = VmStackValue.of(byte)
inline fun VmStackValue(short: Short): VmStackValue.TinyInt = VmStackValue.of(short)
inline fun VmStackValue(int: Int): VmStackValue.TinyInt = VmStackValue.of(int)
inline fun VmStackValue(long: Long): VmStackValue.TinyInt = VmStackValue.of(long)
inline fun VmStackValue(bigInt: BigInt): VmStackValue.Int = VmStackValue.of(bigInt)
inline fun VmStackValue(cell: Cell): VmStackValue.Cell = VmStackValue.of(cell)
inline fun VmStackValue(cellSlice: CellSlice): VmStackValue.Slice = VmStackValue.of(cellSlice)
inline fun VmStackValue(cellBuilder: CellBuilder): VmStackValue.Builder = VmStackValue.of(cellBuilder)
inline fun VmStackValue(cont: VmCont): VmStackValue.Cont = VmStackValue.of(cont)

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

        @JvmStatic
        fun tlbCodec(): TlbCombinator<VmStackValue> = VmStackValueTlbCombinator()
    }
}

private class VmStackValueTlbCombinator : TlbCombinator<VmStackValue>() {
    private val nullConstructor by lazy { VmStackValueNullConstructor() }
    private val tinyIntConstructor by lazy { VmStackValueTinyIntConstructor() }
    private val intConstructor by lazy { VmStackValueIntConstructor() }
    private val nanConstructor by lazy { VmStackValueNanConstructor() }
    private val cellConstructor by lazy { VmStackValueCellConstructor() }
    private val sliceConstructor by lazy { VmStackValueSliceTlbConstructor() }
    private val builderConstructor by lazy { VmStackValueBuilderTlbConstructor() }
    private val contConstructor by lazy { VmStackValueContTlbConstructor() }
    private val tupleConstructor by lazy { VmStackValueTupleConstructor() }

    override val constructors: List<TlbConstructor<out VmStackValue>> by lazy {
        listOf(
            nullConstructor, tinyIntConstructor, intConstructor, nanConstructor, cellConstructor, sliceConstructor,
            builderConstructor, contConstructor, tupleConstructor
        )
    }

    override fun getConstructor(value: VmStackValue): TlbConstructor<out VmStackValue> = when (value) {
        is VmStackValue.Null -> nullConstructor
        is VmStackValue.TinyInt -> tinyIntConstructor
        is VmStackValue.Int -> intConstructor
        is VmStackValue.Nan -> nanConstructor
        is VmStackValue.Cell -> cellConstructor
        is VmStackValue.Slice -> sliceConstructor
        is VmStackValue.Builder -> builderConstructor
        is VmStackValue.Cont -> contConstructor
        is VmStackValue.Tuple -> tupleConstructor
    }

    private class VmStackValueNullConstructor : TlbConstructor<VmStackValue.Null>(
        schema = "vm_stk_null#00 = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.Null
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Null = VmStackValue.Null
    }

    private class VmStackValueTinyIntConstructor : TlbConstructor<VmStackValue.TinyInt>(
        schema = "vm_stk_tinyint#01 value:int64 = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.TinyInt
        ) = cellBuilder {
            storeInt(value.value, 64)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.TinyInt = cellSlice {
            val value = loadInt(64).toLong()
            VmStackValue.TinyInt(value)
        }
    }

    private class VmStackValueIntConstructor : TlbConstructor<VmStackValue.Int>(
        schema = "vm_stk_int#0201_ value:int257 = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.Int
        ) = cellBuilder {
            storeInt(value.value, 257)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Int = cellSlice {
            val value = loadInt(257)
            VmStackValue.Int(value)
        }
    }

    private class VmStackValueNanConstructor : TlbConstructor<VmStackValue.Nan>(
        schema = "vm_stk_nan#02ff = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.Nan
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Nan = VmStackValue.Nan
    }

    private class VmStackValueCellConstructor : TlbConstructor<VmStackValue.Cell>(
        schema = "vm_stk_cell#03 cell:^Cell = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.Cell
        ) = cellBuilder {
            storeRef(value.cell)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Cell = cellSlice {
            val cell = loadRef()
            VmStackValue.Cell(cell)
        }
    }

    private class VmStackValueSliceTlbConstructor : TlbConstructor<VmStackValue.Slice>(
        schema = "vm_stk_slice#04 _:VmCellSlice = VmStackValue;"
    ) {
        private val vmCellSliceCodec by lazy {
            VmCellSlice.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.Slice
        ) = cellBuilder {
            storeTlb(vmCellSliceCodec, value.slice)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Slice = cellSlice {
            val slice = loadTlb(vmCellSliceCodec)
            VmStackValue.Slice(slice)
        }
    }

    private class VmStackValueBuilderTlbConstructor : TlbConstructor<VmStackValue.Builder>(
        schema = "vm_stk_builder#05 cell:^Cell = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackValue.Builder
        ) = cellBuilder {
            storeRef(value.cell)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Builder = cellSlice {
            val cell = loadRef()
            VmStackValue.Builder(cell)
        }
    }

    private class VmStackValueContTlbConstructor : TlbConstructor<VmStackValue.Cont>(
        schema = "vm_stk_cont#06 cont:VmCont = VmStackValue;"
    ) {
        private val vmContCodec by lazy {
            VmCont.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmStackValue.Cont
        ) = cellBuilder {
            storeTlb(vmContCodec, value.cont)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Cont = cellSlice {
            val cont = loadTlb(vmContCodec)
            VmStackValue.Cont(cont)
        }
    }

    private class VmStackValueTupleConstructor : TlbConstructor<VmStackValue.Tuple>(
        schema = "vm_stk_tuple#07 len:(## 16) data:(VmTuple len) = VmStackValue;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmStackValue.Tuple
        ) = cellBuilder {
            storeUInt(value.len, 16)
            storeTlb(VmTuple.tlbCodec(value.len), value.data)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackValue.Tuple = cellSlice {
            val len = loadUInt(16).toInt()
            val data = loadTlb(VmTuple.tlbCodec(len))
            VmStackValue.Tuple(len, data)
        }
    }
}

