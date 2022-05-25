package org.ton.block.tlb

import org.ton.block.Maybe
import org.ton.block.VmControlData
import org.ton.block.VmSaveList
import org.ton.block.VmStack
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun VmControlData.Companion.tlbCodec(): TlbCodec<VmControlData> = VmControlDataTlbConstructor()

private class VmControlDataTlbConstructor : TlbConstructor<VmControlData>(
    schema = "vm_ctl_data\$_ nargs:(Maybe uint13) stack:(Maybe VmStack) save:VmSaveList cp:(Maybe int16) = VmControlData;"
) {
    private val maybeUint13Constructor by lazy {
        Maybe.tlbCodec(UIntTlbConstructor.int(13))
    }
    private val maybeVmStackConstructor by lazy {
        Maybe.tlbCodec(VmStack.tlbCodec())
    }
    private val vmSaveListCodec by lazy {
        VmSaveList.tlbCodec()
    }
    private val maybeInt16Constructor by lazy {
        Maybe.tlbCodec(IntTlbConstructor.int(16))
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmControlData
    ) = cellBuilder {
        storeTlb(maybeUint13Constructor, value.nargs)
        storeTlb(maybeVmStackConstructor, value.stack)
        storeTlb(vmSaveListCodec, value.save)
        storeTlb(maybeInt16Constructor, value.cp)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmControlData = cellSlice {
        val nargs = loadTlb(maybeUint13Constructor)
        val stack = loadTlb(maybeVmStackConstructor)
        val save = loadTlb(vmSaveListCodec)
        val cp = loadTlb(maybeInt16Constructor)
        VmControlData(nargs, stack, save, cp)
    }
}
