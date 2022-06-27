package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("vmc_envelope")
@Serializable
data class VmContEnvelope(
    val cdata: VmControlData,
    val next: VmCont
) : VmCont {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmContEnvelope> = VmContEnvelopeTlbConstructor()
    }
}

private class VmContEnvelopeTlbConstructor : TlbConstructor<VmContEnvelope>(
    schema = "vmc_envelope\$01 cdata:VmControlData next:^VmCont = VmCont;"
) {
    private val vmControlDataCodec by lazy {
        VmControlData.tlbCodec()
    }
    private val vmContCodec by lazy {
        VmCont.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmContEnvelope
    ) = cellBuilder {
        storeTlb(vmControlDataCodec, value.cdata)
        cellBuilder.storeRef {
            storeTlb(vmContCodec, value)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmContEnvelope = cellSlice {
        val cdata = loadTlb(vmControlDataCodec)
        val next = loadRef {
            loadTlb(vmContCodec)
        }
        VmContEnvelope(cdata, next)
    }
}