package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

sealed interface Protocol {
    companion object : TlbCombinatorProvider<Protocol> by ProtocolTlbCombinator
}

private object ProtocolTlbCombinator : TlbCombinator<Protocol>() {
    override val constructors: List<org.ton.tlb.TlbConstructor<out Protocol>> =
        listOf(ProtoHttp.tlbConstructor())

    override fun getConstructor(value: Protocol): org.ton.tlb.TlbConstructor<out Protocol> =
        when (value) {
            is ProtoHttp -> ProtoHttp.tlbConstructor()
        }
}
