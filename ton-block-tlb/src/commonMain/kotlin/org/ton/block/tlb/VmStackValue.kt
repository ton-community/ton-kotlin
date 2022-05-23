package org.ton.block.tlb

import org.ton.block.VmCellSlice
import org.ton.block.VmCont
import org.ton.block.VmStackValue
import org.ton.block.VmTuple
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmStackValue.Companion.tlbCodec(): TlbCodec<VmStackValue> = VmStackValueTlbCombinator

private object VmStackValueTlbCombinator : TlbCombinator<VmStackValue>(
    VmStackValueBuilderTlbConstructor,
    VmStackValueCellConstructor,
    VmStackValueContTlbConstructor,
    VmStackValueIntConstructor,
    VmStackValueNanConstructor,
    VmStackValueNullConstructor,
    VmStackValueSliceTlbConstructor,
    VmStackValueTinyIntConstructor,
    VmStackValueTupleConstructor
) {
    override fun getConstructor(value: VmStackValue): TlbConstructor<out VmStackValue> = when (value) {
        is VmStackValue.Builder -> VmStackValueBuilderTlbConstructor
        is VmStackValue.Cell -> VmStackValueCellConstructor
        is VmStackValue.Cont -> VmStackValueContTlbConstructor
        is VmStackValue.Int -> VmStackValueIntConstructor
        is VmStackValue.Nan -> VmStackValueNanConstructor
        is VmStackValue.Null -> VmStackValueNullConstructor
        is VmStackValue.Slice -> VmStackValueSliceTlbConstructor
        is VmStackValue.TinyInt -> VmStackValueTinyIntConstructor
        is VmStackValue.Tuple -> VmStackValueTupleConstructor
    }

    private object VmStackValueNullConstructor : TlbConstructor<VmStackValue.Null>(
        schema = "vm_stk_null#00 = VmStackValue;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.Null,
            param: Int,
            negativeParam: (Int) -> Unit
        ) {
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.Null = VmStackValue.Null
    }

    private object VmStackValueTinyIntConstructor : TlbConstructor<VmStackValue.TinyInt>(
        schema = "vm_stk_tinyint#01 value:int64 = VmStackValue;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.TinyInt,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeInt(value.value, 64)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.TinyInt = cellSlice {
            val value = loadInt(64).toLong()
            VmStackValue.TinyInt(value)
        }
    }

    private object VmStackValueIntConstructor : TlbConstructor<VmStackValue.Int>(
        schema = "vm_stk_int#0201_ value:int257 = VmStackValue;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.Int,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeInt(value.value, 257)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.Int = cellSlice {
            val value = loadInt(257)
            VmStackValue.Int(value)
        }
    }

    private object VmStackValueNanConstructor : TlbConstructor<VmStackValue.Nan>(
        schema = "vm_stk_nan#02ff = VmStackValue;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.Nan,
            param: Int,
            negativeParam: (Int) -> Unit
        ) {
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.Nan = VmStackValue.Nan
    }

    private object VmStackValueCellConstructor : TlbConstructor<VmStackValue.Cell>(
        schema = "vm_stk_cell#03 cell:^Cell = VmStackValue;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.Cell,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef(value.cell)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.Cell = cellSlice {
            val cell = loadRef()
            VmStackValue.Cell(cell)
        }
    }

    private object VmStackValueSliceTlbConstructor : TlbConstructor<VmStackValue.Slice>(
        schema = "vm_stk_slice#04 _:VmCellSlice = VmStackValue;"
    ) {
        private val vmCellSliceCodec = VmCellSlice.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.Slice,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.slice, vmCellSliceCodec)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.Slice = cellSlice {
            val slice = loadTlb(vmCellSliceCodec)
            VmStackValue.Slice(slice)
        }
    }

    private object VmStackValueBuilderTlbConstructor : TlbConstructor<VmStackValue.Builder>(
        schema = "vm_stk_builder#05 cell:^Cell = VmStackValue;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackValue.Builder,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef(value.cell)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackValue.Builder = cellSlice {
            val cell = loadRef()
            VmStackValue.Builder(cell)
        }
    }

    private object VmStackValueContTlbConstructor : TlbConstructor<VmStackValue.Cont>(
        schema = "vm_stk_cont#06 cont:VmCont = VmStackValue;"
    ) {
        private val vmContCodec = VmCont.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmStackValue.Cont, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeTlb(value.cont, vmContCodec)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmStackValue.Cont = cellSlice {
            val cont = loadTlb(vmContCodec)
            VmStackValue.Cont(cont)
        }
    }

    private object VmStackValueTupleConstructor : TlbConstructor<VmStackValue.Tuple>(
        schema = "vm_stk_tuple#07 len:(## 16) data:(VmTuple len) = VmStackValue;"
    ) {
        private val vmTupleCodec = VmTuple.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmStackValue.Tuple, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeUInt(value.len, 16)
            storeTlb(value.data, vmTupleCodec, value.len)
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmStackValue.Tuple = cellSlice {
            val len = loadUInt(16).toInt()
            val data = loadTlb(vmTupleCodec, len)
            VmStackValue.Tuple(len, data)
        }
    }
}
