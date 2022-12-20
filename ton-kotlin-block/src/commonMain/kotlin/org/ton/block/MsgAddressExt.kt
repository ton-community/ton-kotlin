@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.jvm.JvmStatic

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

private object MsgAddressExtTlbCombinator : TlbCombinator<MsgAddressExt>(
    MsgAddressExt::class,
    AddrNone::class to AddrNone.tlbConstructor(),
    AddrExtern::class to AddrExtern.tlbConstructor(),
)
