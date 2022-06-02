@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.exception.UnknownTlbConstructorException

@Suppress("NOTHING_TO_INLINE")
inline fun MsgAddressExt(externalAddress: BitString? = null): MsgAddressExt = MsgAddressExt.of(externalAddress)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressExt : MsgAddress {
    companion object {
        @JvmStatic
        fun of(externalAddress: BitString? = null): MsgAddressExt {
            return if (externalAddress.isNullOrEmpty()) {
                MsgAddressExtNone
            } else {
                MsgAddressExtOrdinary(externalAddress)
            }
        }

        @JvmStatic
        fun of(externalAddress: ByteArray): MsgAddressExt = MsgAddressExtOrdinary(externalAddress)

        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddressExt> = MsgAddressExtTlbCombinator()
    }
}

private class MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>() {
    private val addrNoneConstructor by lazy { MsgAddressExtNone.tlbCodec() }
    private val addrExternConstructor by lazy { MsgAddressExtOrdinary.tlbCodec() }

    override val constructors: List<TlbConstructor<out MsgAddressExt>> by lazy {
        listOf(addrNoneConstructor, addrExternConstructor)
    }

    @Suppress("REDUNDANT_ELSE_IN_WHEN") // kotlin compiler bug
    override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> = when (value) {
        is MsgAddressExtNone -> addrNoneConstructor
        is MsgAddressExtOrdinary -> addrExternConstructor
        else -> throw UnknownTlbConstructorException()
    }
}
