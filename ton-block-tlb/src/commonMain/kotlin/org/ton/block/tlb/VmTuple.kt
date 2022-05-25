package org.ton.block.tlb

import org.ton.block.VmStackValue
import org.ton.block.VmTuple
import org.ton.block.VmTupleRef
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmTuple.Companion.tlbCodec(n: Int): TlbCodec<VmTuple> = VmTupleCombinator(n)

private class VmTupleCombinator(
    val n: Int
) : TlbCombinator<VmTuple>() {
    private val nilConstructor by lazy { NilTlbConstructor() }
    private val tConsConstructor by lazy { TConsTlbConstructor(n) }

    override val constructors: List<TlbConstructor<out VmTuple>> by lazy {
        listOf(nilConstructor, tConsConstructor)
    }

    override fun getConstructor(value: VmTuple): TlbConstructor<out VmTuple> = when (value) {
        is VmTuple.Nil -> nilConstructor
        is VmTuple.TCons -> tConsConstructor
    }

    override fun loadTlb(cellSlice: CellSlice): VmTuple {
        return if (n == 0) {
            nilConstructor.loadTlb(cellSlice)
        } else {
            tConsConstructor.loadTlb(cellSlice)
        }
    }

    private class NilTlbConstructor : TlbConstructor<VmTuple.Nil>(
        schema = "vm_tuple_nil\$_ = VmTuple 0;"
    ) {
        override fun storeTlb(cellBuilder: CellBuilder, value: VmTuple.Nil) {
        }

        override fun loadTlb(cellSlice: CellSlice): VmTuple.Nil {
            return VmTuple.Nil
        }
    }

    private class TConsTlbConstructor(
        n: Int
    ) : TlbConstructor<VmTuple.TCons>(
        schema = "vm_tuple_tcons\$_ {n:#} head:(VmTupleRef n) tail:^VmStackValue = VmTuple (n + 1);"
    ) {
        private val vmTupleRefCodec by lazy { VmTupleRef.tlbCodec(n - 1) }
        private val vmStackValueCodec by lazy { VmStackValue.tlbCodec() }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmTuple.TCons
        ) = cellBuilder {
            storeTlb(vmTupleRefCodec, value.head)
            storeRef {
                storeTlb(vmStackValueCodec, value.tail)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmTuple.TCons = cellSlice {
            val head = loadTlb(vmTupleRefCodec)
            val tail = loadRef {
                loadTlb(vmStackValueCodec)
            }
            VmTuple.TCons(head, tail)
        }
    }
}
