package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
sealed interface MsgAddress {
    companion object : TlbCombinatorProvider<MsgAddress> by MsgAddressTlbCombinator
}

private object MsgAddressTlbCombinator : TlbCombinator<MsgAddress>(
    MsgAddress::class,
    MsgAddressInt::class to MsgAddressInt,
    MsgAddressExt::class to MsgAddressExt,
)
