package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface Protocol {
    companion object : TlbCombinatorProvider<Protocol> by ProtocolTlbCombinator
}

private object ProtocolTlbCombinator : TlbCombinator<Protocol>(
    Protocol::class,
    ProtoHttp::class to ProtoHttp.tlbConstructor(),
) {
}
