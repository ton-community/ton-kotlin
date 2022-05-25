package org.ton.block.tlb

import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmStackList.Companion.tlbCodec(depth: Int): TlbCodec<VmStackList> = VmStackListCombinator(depth)

private class VmStackListCombinator(val depth: Int) : TlbCombinator<VmStackList>() {
    private val vmStkConsConstructor by lazy {
        VmStackListConsConstructor(depth)
    }
    private val vmStkNilConstructor by lazy {
        VmStackListNilConstructor()
    }

    override val constructors: List<TlbConstructor<out VmStackList>> by lazy {
        listOf(vmStkConsConstructor, vmStkNilConstructor)
    }

    override fun getConstructor(value: VmStackList): TlbConstructor<out VmStackList> = when (value) {
        is VmStackList.Cons -> vmStkConsConstructor
        is VmStackList.Nil -> vmStkNilConstructor
    }

    override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): VmStackList {
        return if (depth == 0) {
            vmStkNilConstructor.decode(cellSlice, param, negativeParam)
        } else {
            vmStkConsConstructor.decode(cellSlice, param, negativeParam)
        }
    }

    private class VmStackListConsConstructor(
        depth: Int
    ) : TlbConstructor<VmStackList.Cons>(
        schema = "vm_stk_cons#_ {n:#} rest:^(VmStackList n) tos:VmStackValue = VmStackList (n + 1);"
    ) {
        val n = depth - 1

        private val vmStackListCodec by lazy {
            VmStackList.tlbCodec(n)
        }
        private val vmStackValue by lazy {
            VmStackValue.tlbCodec()
        }

        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackList.Cons,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            storeRef {
                storeTlb(value.rest, vmStackListCodec, n)
            }
            storeTlb(value.tos, vmStackValue)
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackList.Cons = cellSlice {
            val rest = loadRef {
                loadTlb(vmStackListCodec, n)
            }
            val tos = loadTlb(vmStackValue, n)
            VmStackList.Cons(rest, tos)
        }
    }

    private class VmStackListNilConstructor : TlbConstructor<VmStackList.Nil>(
        schema = "vm_stk_nil#_ = VmStackList 0;"
    ) {
        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackList.Nil,
            param: Int,
            negativeParam: (Int) -> Unit
        ) {
        }

        override fun decode(
            cellSlice: CellSlice,
            param: Int,
            negativeParam: (Int) -> Unit
        ): VmStackList.Nil {
            return VmStackList.Nil
        }
    }
}
