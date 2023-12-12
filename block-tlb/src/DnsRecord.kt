package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

public sealed interface DnsRecord {
    public companion object : TlbCombinatorProvider<DnsRecord> by DnsRecordTlbCombinator
}

private object DnsRecordTlbCombinator : TlbCombinator<DnsRecord>(
    DnsRecord::class,
    DnsNextResolver::class to DnsNextResolver,
    DnsSmcAddress::class to DnsSmcAddress,
    DnsAdnlAddress::class to DnsAdnlAddress,
    DnsText::class to DnsText,
)
