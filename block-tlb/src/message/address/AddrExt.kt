package org.ton.block.message.address

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("addr_extern")
public data class AddrExt(
    val address: BitString // external_address : bits len
) : MsgAddress {
    public constructor(externalAddress: ByteArray) : this(externalAddress.toBitString())

    public companion object : TlbConstructorProvider<AddrExt> by AddrExternTlbConstructor
}

private object AddrExternTlbConstructor : TlbConstructor<AddrExt>(
    schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: AddrExt
    ) = cellBuilder {
        storeUInt(value.address.size, 9)
        storeBits(value.address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrExt = cellSlice {
        val len = loadUInt(9).toInt()
        val externalAddress = loadBits(len)
        AddrExt(externalAddress)
    }
}
