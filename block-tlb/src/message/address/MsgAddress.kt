package org.ton.block.message.address

import kotlinx.serialization.Serializable
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
public sealed interface MsgAddress {
    public companion object : TlbCombinatorProvider<MsgAddress> by MsgAddressTlbCombinator
}

private object MsgAddressTlbCombinator : TlbCombinator<MsgAddress>(
    MsgAddress::class,
    AddrInt::class to AddrInt.tlbCodec(),
    AddrExt::class to AddrExt.tlbConstructor(),
)
