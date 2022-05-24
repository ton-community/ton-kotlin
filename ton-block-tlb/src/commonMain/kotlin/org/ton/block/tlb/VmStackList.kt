package org.ton.block.tlb

import org.ton.block.VmStackList
import org.ton.block.VmStackValue
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmStackList.Companion.tlbCodec(): TlbCodec<VmStackList> = VmStackListCombinator()

private class VmStackListCombinator : TlbCombinator<VmStackList>(
    VmStackListConsConstructor, VmStackListNilConstructor
) {
    override fun getConstructor(value: VmStackList): TlbConstructor<out VmStackList> = when (value) {
        is VmStackList.Cons -> VmStackListConsConstructor
        is VmStackList.Nil -> VmStackListNilConstructor
    }

    private object VmStackListConsConstructor : TlbConstructor<VmStackList.Cons>(
        schema = "vm_stk_cons#_ {n:#} rest:^(VmStackList n) tos:VmStackValue = VmStackList (n + 1);"
    ) {
        private val vmStackListCodec = VmStackList.tlbCodec()
        private val vmStackValue = VmStackValue.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder,
            value: VmStackList.Cons,
            param: Int,
            negativeParam: (Int) -> Unit
        ) = cellBuilder {
            val n = param - 1
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
            val n = param - 1
            val rest = loadRef {
                loadTlb(vmStackListCodec, n)
            }
            val tos = loadTlb(vmStackValue, n)
            VmStackList.Cons(rest, tos)
        }
    }

    private object VmStackListNilConstructor : TlbConstructor<VmStackList.Nil>(
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
