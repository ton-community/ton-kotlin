package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("vm_ctl_data")
@Serializable
data class VmControlData(
    val nargs: Maybe<Int>,
    val stack: Maybe<VmStack>,
    val save: VmSaveList,
    val cp: Maybe<Int>
) {
    constructor(nargs: Int?, stack: VmStack?, save: VmSaveList, cp: Int?) : this(
        nargs.toMaybe(),
        stack.toMaybe(),
        save,
        cp.toMaybe()
    )

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmControlData> = VmControlDataTlbConstructor()
    }
}

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