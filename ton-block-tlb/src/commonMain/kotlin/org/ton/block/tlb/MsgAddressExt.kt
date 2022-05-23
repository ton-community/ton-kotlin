package org.ton.block.tlb

import org.ton.block.MsgAddressExt
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

object MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>(

) {
    override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> {
        TODO("Not yet implemented")
    }
}
