package org.ton.block.tlb

import org.ton.block.VmStackValue
import org.ton.block.VmTuple
import org.ton.block.VmTupleRef
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.*

fun VmTupleRef.Companion.tlbCodec(n: Int): TlbCodec<VmTupleRef> = VmTupleRefTlbCombinator(n)

private class VmTupleRefTlbCombinator(val n: Int) : TlbCombinator<VmTupleRef>() {
    private val nilConstructor by lazy { VmTupleRefNilTlbConstructor() }
    private val singleConstructor by lazy { VmTupleRefSingleTlbConstructor() }
    private val anyConstructor by lazy { VmTupleRefAnyTlbConstructor(n) }

    override val constructors: List<TlbConstructor<out VmTupleRef>> by lazy {
        listOf(nilConstructor, singleConstructor, anyConstructor)
    }

    override fun getConstructor(value: VmTupleRef): TlbConstructor<out VmTupleRef> = when (value) {
        is VmTupleRef.Nil -> nilConstructor
        is VmTupleRef.Single -> singleConstructor
        is VmTupleRef.Any -> anyConstructor
    }

    override fun loadTlb(cellSlice: CellSlice): VmTupleRef {
        return when (n) {
            0 -> nilConstructor.loadTlb(cellSlice)
            1 -> singleConstructor.loadTlb(cellSlice)
            else -> anyConstructor.loadTlb(cellSlice)
        }
    }

    private class VmTupleRefNilTlbConstructor : TlbConstructor<VmTupleRef.Nil>(
        schema = "vm_tupref_nil\$_ = VmTupleRef 0;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmTupleRef.Nil
        ) {
        }

        override fun loadTlb(cellSlice: CellSlice): VmTupleRef.Nil {
            return VmTupleRef.Nil
        }
    }

    private class VmTupleRefSingleTlbConstructor : TlbConstructor<VmTupleRef.Single>(
        schema = "vm_tupref_single\$_ entry:^VmStackValue = VmTupleRef 1;"
    ) {
        private val vmStackValueCodec by lazy { VmStackValue.tlbCodec() }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmTupleRef.Single
        ) = cellBuilder {
            storeRef {
                storeTlb(vmStackValueCodec, value.entry)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmTupleRef.Single = cellSlice {
            val entry = loadRef {
                loadTlb(vmStackValueCodec)
            }
            VmTupleRef.Single(entry)
        }
    }

    private class VmTupleRefAnyTlbConstructor(
        n: Int
    ) : TlbConstructor<VmTupleRef.Any>(
        schema = "vm_tupref_any\$_ {n:#} ref:^(VmTuple (n + 2)) = VmTupleRef (n + 2);"
    ) {
        private val vmTupleCodec by lazy { VmTuple.tlbCodec(n - 2) }

        override fun storeTlb(
            cellBuilder: CellBuilder, value: VmTupleRef.Any
        ) = cellBuilder {
            storeRef {
                storeTlb(vmTupleCodec, value.ref)
            }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmTupleRef.Any = cellSlice {
            val ref = loadRef {
                loadTlb(vmTupleCodec)
            }
            VmTupleRef.Any(ref)
        }
    }
}
