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

fun VmControlData.Companion.tlbCodec(): TlbCodec<VmControlData> = VmControlDataTlbConstructor

private object VmControlDataTlbConstructor : TlbConstructor<VmControlData>(
    schema = "vm_ctl_data\$_ nargs:(Maybe uint13) stack:(Maybe VmStack) save:VmSaveList cp:(Maybe int16) = VmControlData;"
) {
    private val maybeUint13Constructor = Maybe.tlbCodec(UIntTlbConstructor.int(13))
    private val maybeVmStackConstructor = Maybe.tlbCodec(VmStack.tlbCodec())
    private val vmSaveListCodec = VmSaveList.tlbCodec()
    private val maybeInt16Constructor = Maybe.tlbCodec(IntTlbConstructor.int(16))

    override fun encode(
        cellBuilder: CellBuilder,
        value: VmControlData,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.nargs, maybeUint13Constructor)
        storeTlb(value.stack, maybeVmStackConstructor)
        storeTlb(value.save, vmSaveListCodec)
        storeTlb(value.cp, maybeInt16Constructor)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmControlData = cellSlice {
        val nargs = loadTlb(maybeUint13Constructor)
        val stack = loadTlb(maybeVmStackConstructor)
        val save = loadTlb(vmSaveListCodec)
        val cp = loadTlb(maybeInt16Constructor)
        VmControlData(nargs, stack, save, cp)
    }
}
