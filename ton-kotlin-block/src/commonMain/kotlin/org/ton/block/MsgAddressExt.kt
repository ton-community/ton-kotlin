@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

inline fun MsgAddressExt(externalAddress: BitString? = null): MsgAddressExt = MsgAddressExt.of(externalAddress)
inline fun MsgAddressExt(externalAddress: ByteArray): MsgAddressExt = MsgAddressExt.of(externalAddress)

@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressExt : MsgAddress {
    companion object : TlbCodec<MsgAddressExt> by MsgAddressExtTlbCombinator {
        @JvmStatic
        fun of(externalAddress: BitString? = null): MsgAddressExt {
            return if (externalAddress.isNullOrEmpty()) {
                AddrNone
            } else {
                AddrExtern(externalAddress)
            }
        }

        @JvmStatic
        fun of(externalAddress: ByteArray): MsgAddressExt = AddrExtern(externalAddress)

        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddressExt> = MsgAddressExtTlbCombinator
    }
}

private object MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>() {
    private val addrNoneConstructor = AddrNone.tlbCodec()
    private val addrExternConstructor = AddrExtern.tlbCodec()

    override val constructors: List<TlbConstructor<out MsgAddressExt>> by lazy {
        listOf(addrNoneConstructor, addrExternConstructor)
    }

    override fun getConstructor(value: MsgAddressExt): TlbConstructor<out MsgAddressExt> = when (value) {
        is AddrNone -> addrNoneConstructor
        is AddrExtern -> addrExternConstructor
    }
}
