package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

@SerialName("vmc_std")
@Serializable
public data class VmContStd(
    val cdata: VmControlData,
    val code: VmCellSlice
) : VmCont {
    public companion object : TlbCodec<VmContStd> by VmContStdTlbConstructor {
        @JvmStatic
        public fun tlbCodec(): TlbConstructor<VmContStd> = VmContStdTlbConstructor
    }
}

private object VmContStdTlbConstructor : TlbConstructor<VmContStd>(
    schema = "vmc_std\$00 cdata:VmControlData code:VmCellSlice = VmCont;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmContStd
    ) = cellBuilder {
        storeTlb(VmControlData, value.cdata)
        storeTlb(VmCellSlice, value.code)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmContStd = cellSlice {
        val cdata = loadTlb(VmControlData)
        val code = loadTlb(VmCellSlice)
        VmContStd(cdata, code)
    }
}
