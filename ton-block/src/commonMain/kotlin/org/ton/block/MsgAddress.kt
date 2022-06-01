package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
sealed interface MsgAddress {
    companion object {
        fun tlbCodec(): TlbCodec<MsgAddress> = MsgAddressTlbConstructor()
    }

    private class MsgAddressTlbConstructor : TlbCombinator<MsgAddress>() {
        private val msgAddressInt by lazy {
            MsgAddressInt.Companion.MsgAddressIntTlbCombinator()
        }
        private val msgAddressExt by lazy {
            MsgAddressExt.Companion.MsgAddressExtTlbCombinator()
        }

        override val constructors: List<TlbConstructor<out MsgAddress>> by lazy {
            msgAddressInt.constructors + msgAddressExt.constructors
        }

        override fun getConstructor(value: MsgAddress): TlbConstructor<out MsgAddress> {
            return when (value) {
                is MsgAddressInt -> msgAddressInt.getConstructor(value)
                is MsgAddressExt -> msgAddressExt.getConstructor(value)
            }
        }
    }
}

