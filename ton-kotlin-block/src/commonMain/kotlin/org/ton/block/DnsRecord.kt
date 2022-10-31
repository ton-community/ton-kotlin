package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface DnsRecord {
    companion object : TlbCombinatorProvider<DnsRecord> by DnsRecordTlbCombinator
}

private object DnsRecordTlbCombinator : TlbCombinator<DnsRecord>() {
    override val constructors: List<TlbConstructor<out DnsRecord>>
        get() = listOf(
            DnsNextResolver.tlbConstructor(),
            DnsSmcAddress.tlbConstructor(),
            DnsAdnlAddress.tlbConstructor(),
            DnsText.tlbConstructor(),
        )
}
