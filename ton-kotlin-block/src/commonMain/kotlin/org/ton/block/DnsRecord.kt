package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface DnsRecord {
    companion object : TlbCombinatorProvider<DnsRecord> by DnsRecordTlbCombinator
}

private object DnsRecordTlbCombinator : TlbCombinator<DnsRecord>(
    DnsRecord::class,
    DnsNextResolver::class to DnsNextResolver,
    DnsSmcAddress::class to DnsSmcAddress,
    DnsAdnlAddress::class to DnsAdnlAddress,
    DnsText::class to DnsText,
)
