package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("interm_addr_simple")
data class IntermediateAddressSimple(
    val workchain_id: Int,
    val addr_pfx: Long
) : IntermediateAddress {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<IntermediateAddressSimple> = IntermediateAddressSimpleTlbConstructor
    }
}

private object IntermediateAddressSimpleTlbConstructor : TlbConstructor<IntermediateAddressSimple>(
    schema = "interm_addr_simple\$10 workchain_id:int8 addr_pfx:uint64 = IntermediateAddress;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: IntermediateAddressSimple
    ) = cellBuilder {
        storeInt(value.workchain_id, 8)
        storeUInt(value.addr_pfx, 64)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): IntermediateAddressSimple = cellSlice {
        val workchainId = loadInt(8).toInt()
        val addrPfx = loadUInt(64).toLong()
        IntermediateAddressSimple(workchainId, addrPfx)
    }
}
