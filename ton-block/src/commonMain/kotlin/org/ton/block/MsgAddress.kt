package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.exception.UnknownTlbConstructorException

@Serializable
sealed interface MsgAddress {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddress> = MsgAddressTlbCombinator()
    }
}

private class MsgAddressTlbCombinator : TlbCombinator<MsgAddress>() {
    private val msgAddressInt by lazy {
        MsgAddressInt.tlbCodec()
    }
    private val msgAddressExt by lazy {
        MsgAddressExt.tlbCodec()
    }

    override val constructors: List<TlbConstructor<out MsgAddress>> by lazy {
        msgAddressInt.constructors + msgAddressExt.constructors
    }

    override fun getConstructor(value: MsgAddress): TlbConstructor<out MsgAddress> {
        return when (value) {
            is MsgAddressInt -> msgAddressInt.getConstructor(value)
            is MsgAddressExt -> msgAddressExt.getConstructor(value)
            else -> throw UnknownTlbConstructorException()
        }
    }
}

