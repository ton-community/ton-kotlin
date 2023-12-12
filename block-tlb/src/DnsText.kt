package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
public class DnsText(
    @SerialName("_")
    public val value: Text
) : DnsRecord {
    public companion object : TlbConstructorProvider<DnsText> by DnsTextTlbConstructor
}

private object DnsTextTlbConstructor : TlbConstructor<DnsText>(
    schema = "dns_text#1eda _:Text = DNSRecord;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DnsText) {
        cellBuilder.storeTlb(Text, value.value)
    }

    override fun loadTlb(cellSlice: CellSlice): DnsText {
        val value = cellSlice.loadTlb(Text)
        return DnsText(value)
    }
}
