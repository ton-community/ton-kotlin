package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@SerialName("dns_adnl_address")
@Serializable
data class DnsAdnlAddress(
    val adnl_addr: BitString,
    val flags: BitString,
    val proto_list: ProtoList?
) : DnsRecord {
    init {
        require(adnl_addr.size == 256) { "expected adnl_addr.size: 256, actual: ${adnl_addr.size}" }
        require(flags.size == 8) { "expected flags.size: 8, actual: ${flags.size}" }
        require(!flags[0] || proto_list != null) { "proto_list required, if flags[0] == true" }
    }

    companion object : TlbConstructorProvider<DnsAdnlAddress> by DnsAdnlAddressTlbConstructor
}

private object DnsAdnlAddressTlbConstructor : org.ton.tlb.TlbConstructor<DnsAdnlAddress>(
    schema = "dns_adnl_address#ad01 adnl_addr:bits256 flags:(## 8) { flags <= 1 } proto_list:flags.0?ProtoList = DNSRecord;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: DnsAdnlAddress
    ) {
        cellBuilder.storeBits(value.adnl_addr)
        cellBuilder.storeBits(value.flags)
        if (value.flags[0]) {
            cellBuilder.storeTlb(ProtoList, value.proto_list!!)
        }
    }

    override fun loadTlb(
        cellSlice: org.ton.cell.CellSlice
    ): DnsAdnlAddress {
        val adnl_addr = cellSlice.loadBits(256)
        val flags = cellSlice.loadBits(8)
        val proto_list = if (flags[0]) {
            cellSlice.loadTlb(ProtoList)
        } else {
            null
        }
        return DnsAdnlAddress(adnl_addr, flags, proto_list)
    }
}
