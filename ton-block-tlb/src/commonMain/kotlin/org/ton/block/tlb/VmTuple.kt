package org.ton.block.tlb

import org.ton.block.VmStackValue
import org.ton.block.VmTuple
import org.ton.block.VmTupleRef
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmTuple.Companion.tlbCodec(): TlbCodec<VmTuple> = VmTupleCombinator

private object VmTupleCombinator : TlbCombinator<VmTuple>(
    NilTlbConstructor, TConsTlbConstructor
) {
    override fun getConstructor(value: VmTuple): TlbConstructor<out VmTuple> = when (value) {
        is VmTuple.Nil -> NilTlbConstructor
        is VmTuple.TCons -> TConsTlbConstructor
    }

    object NilTlbConstructor : TlbConstructor<VmTuple.Nil>(
        schema = "vm_tuple_nil\$_ = VmTuple 0;"
    ) {
        override fun encode(cellBuilder: CellBuilder, value: VmTuple.Nil, param: Int, negativeParam: (Int) -> Unit) {
        }

        override fun decode(cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit): VmTuple.Nil {
            return VmTuple.Nil
        }
    }

    private object TConsTlbConstructor : TlbConstructor<VmTuple.TCons>(
        schema = "vm_tuple_tcons\$_ {n:#} head:(VmTupleRef n) tail:^VmStackValue = VmTuple (n + 1);"
    ) {
        private val vmTupleRefCodec = VmTupleRef.tlbCodec()
        private val vmStackValueCodec = VmStackValue.tlbCodec()

        override fun encode(
            cellBuilder: CellBuilder, value: VmTuple.TCons, param: Int, negativeParam: (Int) -> Unit
        ) = cellBuilder {
            val n = param - 1
            storeTlb(value.head, vmTupleRefCodec, n)
            storeRef {
                storeTlb(value.tail, vmStackValueCodec)
            }
        }

        override fun decode(
            cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
        ): VmTuple.TCons = cellSlice {
            val n = param - 1
            val head = loadTlb(vmTupleRefCodec, n)
            val tail = loadRef {
                loadTlb(vmStackValueCodec)
            }
            VmTuple.TCons(head, tail)
        }
    }
}
