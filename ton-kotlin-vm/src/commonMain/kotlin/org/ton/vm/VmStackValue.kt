package org.ton.vm

import org.ton.bigint.BigInt
import org.ton.bigint.toBigInt
import org.ton.cell.Cell
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import org.ton.vm.exception.TvmIntegerOverflowException

public interface VmStackValue : TlbObject {
    public interface VmStkNumber : VmStackValue {
        public fun toBigInt(): BigInt
        public fun toLong(): Long
        public fun toInt(): Int
        public fun toBoolean(): Boolean
    }

    /**
     * ```tl-b
     * vm_stk_null#00 = VmStackValue;
     */
    public object VmStkNull : VmStackValue {
        override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
            return printer.type("vm_stk_null")
        }

        override fun toString(): String = print().toString()
    }

    /**
     * ```tl-b
     * vm_stk_tinyint#01 value:int64 = VmStackValue;
     */
    public interface VmStkTinyInt : VmStkNumber {
        public val value: Long
    }

    /**
     * ```tl-b
     * vm_stk_int#0201_ value:int257 = VmStackValue;
     */
    public interface VmStkInt : VmStkNumber {
        public val value: BigInt
    }

    /**
     * ```tl-b
     * vm_stk_nan#02ff = VmStackValue;
     */
    public interface VmStkNan : VmStkNumber

    /**
     * vm_stk_cell#03 cell:^Cell = VmStackValue;
     */
    public interface VmStkCell : VmStackValue {
        public val cell: Cell
    }

    /**
     * ```tl-b
     * vm_stk_slice#04 _:VmCellSlice = VmStackValue;
     */
    public interface VmStkSlice : VmStackValue {
        public val value: VmCellSlice
    }

    /**
     * ```tl-b
     * vm_stk_cont#06 cont:VmCont = VmStackValue;
     */
    public interface VmStkCont : VmStackValue {
        public val cont: VmCont
    }

    /**
     * ```tl-b
     * vm_stk_tuple#07 len:(## 16) data:(VmTuple len) = VmStackValue;
     */
    public interface VmStkTuple : VmStackValue {
        public val len: Int
        public val data: VmTuple
    }

    public companion object {
        public fun int(value: BigInt): VmStkInt = VmStkIntImpl(value)
        public fun int(value: Long): VmStkTinyInt = VmStkTinyIntImpl(value)
        public fun tinyInt(value: Long): VmStkTinyInt = VmStkTinyIntImpl(value)
        public fun nan(): VmStkNan = VmStkNanImpl
        public fun cell(cell: Cell): VmStkCell = VmStkCellImpl(cell)
        public fun slice(slice: VmCellSlice): VmStkSlice = VmStkSliceImpl(slice)
        public fun cont(cont: VmCont): VmStkCont = VmContImpl(cont)
        public fun tuple(len: Int, data: VmTuple): VmStkTuple = VmStkTupleImpl(len, data)
    }
}

private data class VmStkIntImpl(override val value: BigInt) : VmStackValue.VmStkInt {
    override fun toBigInt(): BigInt = value
    override fun toLong(): Long = value.toLong()
    override fun toInt(): Int = value.toInt()
    override fun toBoolean(): Boolean = value != 0.toBigInt()
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("vm_stk_int") {
            field("value", value)
        }
    }
}

private data class VmStkTinyIntImpl(override val value: Long) : VmStackValue.VmStkTinyInt {
    override fun toBigInt(): BigInt = value.toBigInt()
    override fun toLong(): Long = value
    override fun toInt(): Int = value.toInt()
    override fun toBoolean(): Boolean = value != 0L

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("vm_stk_tinyint") {
            field("value", value)
        }
    }
    override fun toString(): String = print().toString()
}

private data class VmStkCellImpl(override val cell: Cell) : VmStackValue.VmStkCell {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("vm_stk_cell") {
            field("cell", cell)
        }
    }
    override fun toString(): String = print().toString()
}

private data class VmStkSliceImpl(override val value: VmCellSlice) : VmStackValue.VmStkSlice {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("vm_stk_slice") {
            field("value", value)
        }
    }
    override fun toString(): String = print().toString()
}

private data class VmContImpl(override val cont: VmCont) : VmStackValue.VmStkCont {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("vm_stk_cont") {
            field("cont", cont)
        }
    }
    override fun toString(): String = print().toString()
}

private data class VmStkTupleImpl(override val len: Int, override val data: VmTuple) : VmStackValue.VmStkTuple {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("vm_stk_tuple") {
            field("len", len)
            field("data", data)
        }
    }
    override fun toString(): String = print().toString()
}

private object VmStkNanImpl : VmStackValue.VmStkNan {
    override fun toBigInt(): BigInt = throw TvmIntegerOverflowException()
    override fun toLong(): Long = throw TvmIntegerOverflowException()
    override fun toInt(): Int = throw TvmIntegerOverflowException()
    override fun toBoolean(): Boolean = throw TvmIntegerOverflowException()
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("vm_stk_nan")
    override fun toString(): String = print().toString()
}
