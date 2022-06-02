package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@SerialName("addr_extern")
@Serializable
data class MsgAddressExtOrdinary(
    val len: Int,
    @SerialName("external_address")
    val externalAddress: BitString
) : MsgAddressExt {
    init {
        require(externalAddress.size == len) { "externalAddress.size expected: $len actual: ${externalAddress.size}" }
    }

    constructor(externalAddress: ByteArray) : this(externalAddress.toBitString())
    constructor(externalAddress: BitString) : this(externalAddress.size, externalAddress)

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<MsgAddressExtOrdinary> = AddrExternTlbConstructor
    }
}

private object AddrExternTlbConstructor : TlbConstructor<MsgAddressExtOrdinary>(
    schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: MsgAddressExtOrdinary
    ) = cellBuilder {
        storeUInt(value.len, 9)
        storeBits(value.externalAddress)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): MsgAddressExtOrdinary = cellSlice {
        val len = loadUInt(9).toInt()
        val externalAddress = loadBitString(len)
        MsgAddressExtOrdinary(len, externalAddress)
    }
}
