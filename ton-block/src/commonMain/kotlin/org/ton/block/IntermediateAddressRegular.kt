package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("interm_addr_regular")
data class IntermediateAddressRegular(
    val use_dest_bits: Int
) : IntermediateAddress {
    init {
        require(use_dest_bits <= 96) { "expected: use_dest_bits <= 96, actual: $use_dest_bits" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<IntermediateAddressRegular> = IntermediateAddressRegularTlbConstructor
    }
}

private object IntermediateAddressRegularTlbConstructor : TlbConstructor<IntermediateAddressRegular>(
    schema = "interm_addr_regular\$0 use_dest_bits:(#<= 96) = IntermediateAddress;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: IntermediateAddressRegular
    ) = cellBuilder {
        storeUIntLeq(value.use_dest_bits, 96)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): IntermediateAddressRegular = cellSlice {
        val useDestBits = loadUIntLeq(96).toInt()
        IntermediateAddressRegular(useDestBits)
    }
}