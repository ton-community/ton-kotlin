package org.ton.block

import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface ProtoList : Iterable<Protocol> {
    companion object : TlbCombinatorProvider<ProtoList> by ProtoListTlbCombinator
}

private object ProtoListTlbCombinator : org.ton.tlb.TlbCombinator<ProtoList>(
    ProtoList::class,
    ProtoListNil::class to ProtoListNil.tlbConstructor(),
    ProtoListNext::class to ProtoListNext.tlbConstructor(),
)
