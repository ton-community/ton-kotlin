package org.ton.block

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


public sealed interface MsgAddress : TlbObject {
    public companion object : TlbCombinatorProvider<MsgAddress> by MsgAddressTlbCombinator
}

private object MsgAddressTlbCombinator : TlbCombinator<MsgAddress>(
    MsgAddress::class,
    MsgAddressInt::class to MsgAddressInt.tlbCodec(),
    MsgAddressExt::class to MsgAddressExt.tlbCodec(),
)
