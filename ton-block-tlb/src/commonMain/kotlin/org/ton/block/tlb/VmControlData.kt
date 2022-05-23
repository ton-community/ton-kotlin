package org.ton.block.tlb

import org.ton.block.VmControlData
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

object VmControlDataTlbConstructor : TlbConstructor<VmControlData>(
    schema = "vm_ctl_data\$_ nargs:(Maybe uint13) stack:(Maybe VmStack) save:VmSaveList cp:(Maybe int16) = VmControlData;"
) {
    private val maybeUint13Constructor = MaybeTlbCombinator(UIntTlbConstructor.int(13))
    private val maybeVmStackConstructor = MaybeTlbCombinator(VmStackTlbConstructor)
    private val maybeInt16Constructor = MaybeTlbCombinator(IntTlbConstructor.int(16))

    override fun encode(
        cellBuilder: CellBuilder,
        value: VmControlData,
        param: Int,
        negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.nargs, maybeUint13Constructor)
        storeTlb(value.stack, maybeVmStackConstructor)
        storeTlb(value.save, VmSaveListTlbConstructor)
        storeTlb(value.cp, maybeInt16Constructor)
    }

    override fun decode(
        cellSlice: CellSlice,
        param: Int,
        negativeParam: (Int) -> Unit
    ): VmControlData = cellSlice {
        val nargs = loadTlb(maybeUint13Constructor)
        val stack = loadTlb(maybeVmStackConstructor)
        val save = loadTlb(VmSaveListTlbConstructor)
        val cp = loadTlb(maybeInt16Constructor)
        VmControlData(nargs, stack, save, cp)
    }
}

fun VmControlData.Companion.tlbCodec(): TlbCodec<VmControlData> = VmControlDataTlbConstructor
