@file:Suppress("NOTHING_TO_INLINE", "PropertyName")

package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

inline fun MsgAddressInt(address: String): MsgAddressInt = MsgAddressInt.parse(address)

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
public sealed interface MsgAddressInt : MsgAddress {
    val workchainId: Int

    companion object : TlbCodec<MsgAddressInt> by MsgAddressIntTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddressInt> = MsgAddressIntTlbCombinator

        @JvmStatic
        fun toString(
            address: MsgAddressInt,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            checkAddressStd(address)
            return AddrStd.toString(address, userFriendly, urlSafe, testOnly, bounceable)
        }

        @JvmStatic
        fun parse(address: String): MsgAddressInt = AddrStd.parse(address)

        @JvmStatic
        fun parseRaw(address: String): MsgAddressInt = AddrStd.parseRaw(address)

        @JvmStatic
        public fun parseUserFriendly(address: String): MsgAddressInt = AddrStd.parseUserFriendly(address)

        private fun checkAddressStd(value: MsgAddressInt) {
            contract {
                returns() implies (value is AddrStd)
            }
            require(value is AddrStd) {
                "expected class: ${AddrStd::class} actual: ${value::class}"
            }
        }
    }
}

private object MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>(
    MsgAddressInt::class,
    AddrStd::class to AddrStd.tlbCodec(),
    AddrVar::class to AddrVar.tlbCodec()
)
