package org.ton.block.tlb

import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmStackList.Companion.tlbCodec(n: Int): TlbCodec<VmStackList> = VmStackListCombinator(n)

private class VmStackListCombinator(val n: Int) : TlbCombinator<VmStackList>() {
    private val vmStkConsConstructor by lazy {
        VmStackListConsConstructor(n)
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

    override fun loadTlb(cellSlice: CellSlice): VmStackList {
        return if (n == 0) {
            vmStkNilConstructor.loadTlb(cellSlice)
        } else {
            vmStkConsConstructor.loadTlb(cellSlice)
        }
    }

    private class VmStackListConsConstructor(
        n: Int
    ) : TlbConstructor<VmStackList.Cons>(
        schema = "vm_stk_cons#_ {n:#} rest:^(VmStackList n) tos:VmStackValue = VmStackList (n + 1);"
    ) {
        private val vmStackListCodec by lazy {
            VmStackList.tlbCodec(n - 1)
        }
        private val vmStackValue by lazy {
            VmStackValue.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackList.Cons
        ) = cellBuilder {
            storeRef {
                storeTlb(vmStackListCodec, value.rest)
            }
            storeTlb(vmStackValue, value.tos)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackList.Cons = cellSlice {
            val rest = loadRef {
                loadTlb(vmStackListCodec)
            }
            val tos = loadTlb(vmStackValue)
            VmStackList.Cons(rest, tos)
        }
    }

    private class VmStackListNilConstructor : TlbConstructor<VmStackList.Nil>(
        schema = "vm_stk_nil#_ = VmStackList 0;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackList.Nil
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackList.Nil {
            return VmStackList.Nil
        }
    }
}
