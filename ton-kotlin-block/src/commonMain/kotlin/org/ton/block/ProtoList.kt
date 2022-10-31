package org.ton.block

import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface ProtoList : Iterable<Protocol> {
    companion object : TlbCombinatorProvider<ProtoList> by ProtoListTlbCombinator
}

private object ProtoListTlbCombinator : org.ton.tlb.TlbCombinator<ProtoList>() {
    override val constructors: List<TlbConstructor<out ProtoList>> =
        listOf(ProtoListNil.tlbConstructor(), ProtoListNext.tlbConstructor())

    override fun getConstructor(value: ProtoList): TlbConstructor<out ProtoList> =
        when (value) {
            is ProtoListNil -> ProtoListNil.tlbConstructor()
            is ProtoListNext -> ProtoListNext.tlbConstructor()
        }
}
