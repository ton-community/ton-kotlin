package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
sealed interface MsgAddress {
    companion object : TlbCombinatorProvider<MsgAddress> by MsgAddressTlbCombinator
}

private object MsgAddressTlbCombinator : TlbCombinator<MsgAddress>() {
    private val msgAddressInt = MsgAddressInt.tlbCodec()
    private val msgAddressExt = MsgAddressExt.tlbCodec()

    override val constructors: List<TlbConstructor<out MsgAddress>> =
        msgAddressInt.constructors + msgAddressExt.constructors

    override fun getConstructor(value: MsgAddress): TlbConstructor<out MsgAddress> {
        return when (value) {
            is MsgAddressInt -> msgAddressInt.getConstructor(value)
            is MsgAddressExt -> msgAddressExt.getConstructor(value)
        }
    }
}

