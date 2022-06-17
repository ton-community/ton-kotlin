package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("vmc_std")
@Serializable
data class VmContStd(
    val cdata: VmControlData,
    val code: VmCellSlice
) : VmCont {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmContStd> = VmContStdTlbConstructor()
    }
}

private class VmContStdTlbConstructor : TlbConstructor<VmContStd>(
    schema = "vmc_std\$00 cdata:VmControlData code:VmCellSlice = VmCont;"
) {
    private val vmControlDataCodec by lazy {
        VmControlData.tlbCodec()
    }
    private val vmCellSliceCodec by lazy {
        VmCellSlice.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmContStd
    ) = cellBuilder {
        storeTlb(vmControlDataCodec, value.cdata)
        storeTlb(vmCellSliceCodec, value.code)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmContStd = cellSlice {
        val cdata = loadTlb(vmControlDataCodec)
        val code = loadTlb(vmCellSliceCodec)
        VmContStd(cdata, code)
    }
}