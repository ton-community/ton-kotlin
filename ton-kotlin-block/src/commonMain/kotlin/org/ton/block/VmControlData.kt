package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.IntTlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@SerialName("vm_ctl_data")
@Serializable
class VmControlData(
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

    companion object : TlbCodec<VmControlData> by VmControlDataTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmControlData> = VmControlDataTlbConstructor
    }
}

private object VmControlDataTlbConstructor : TlbConstructor<VmControlData>(
    schema = "vm_ctl_data\$_ nargs:(Maybe uint13) stack:(Maybe VmStack) save:VmSaveList cp:(Maybe int16) = VmControlData;"
) {
    private val maybeUint13Constructor = Maybe.tlbCodec(UIntTlbConstructor.int(13))
    private val maybeVmStackConstructor = Maybe.tlbCodec(VmStack.tlbCodec())
    private val maybeInt16Constructor = Maybe.tlbCodec(IntTlbConstructor.int(16))

    @Suppress("UNCHECKED_CAST")
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmControlData
    ) = cellBuilder {
        storeTlb(maybeUint13Constructor, value.nargs)
        storeTlb(maybeVmStackConstructor, value.stack)
        storeTlb(VmSaveList, value.save)
        storeTlb(maybeInt16Constructor, value.cp)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmControlData = cellSlice {
        val nargs = loadTlb(maybeUint13Constructor)
        val stack = loadTlb(maybeVmStackConstructor)
        val save = loadTlb(VmSaveList)
        val cp = loadTlb(maybeInt16Constructor)
        VmControlData(nargs, stack, save, cp)
    }
}
