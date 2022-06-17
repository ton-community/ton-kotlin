package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("addr_extern")
data class AddrExtern(
    val len: Int,
    val external_address: BitString
) : MsgAddressExt {
    init {
        require(external_address.size == len) { "required: external_address.size == len, actual: ${external_address.size}" }
    }

    constructor(externalAddress: ByteArray) : this(externalAddress.toBitString())
    constructor(externalAddress: BitString) : this(externalAddress.size, externalAddress)

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AddrExtern> = AddrExternTlbConstructor
    }
}

private object AddrExternTlbConstructor : TlbConstructor<AddrExtern>(
    schema = "addr_extern\$01 len:(## 9) external_address:(bits len) = MsgAddressExt;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: AddrExtern
    ) = cellBuilder {
        storeUInt(value.len, 9)
        storeBits(value.external_address)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AddrExtern = cellSlice {
        val len = loadUInt(9).toInt()
        val externalAddress = loadBitString(len)
        AddrExtern(len, externalAddress)
    }
}
