package org.ton.block

import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

data class DnsSmcAddress(
    val smc_address: MsgAddressInt,
    val flags: BitString,
    val cap_list: SmcCapList?
) : DnsRecord {
    companion object : TlbConstructorProvider<DnsSmcAddress> by DnsSmcAddressTlbConstructor
}

private object DnsSmcAddressTlbConstructor : TlbConstructor<DnsSmcAddress>(
    schema = "dns_smc_address#9fd3 smc_address:MsgAddressInt flags:(## 8) cap_list:flags.0?SmcCapList = DNSRecord;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DnsSmcAddress) {
        cellBuilder.storeTlb(MsgAddressInt, value.smc_address)
        cellBuilder.storeBits(value.flags)
        if (value.flags[0]) {
            cellBuilder.storeTlb(SmcCapList, value.cap_list!!)
        }
    }

    override fun loadTlb(cellSlice: CellSlice): DnsSmcAddress {
        val smc_address = cellSlice.loadTlb(MsgAddressInt)
        val flags = cellSlice.loadBits(8)
        val cap_list = if (flags[0]) cellSlice.loadTlb(SmcCapList) else null
        return DnsSmcAddress(smc_address, flags, cap_list)
    }
}
