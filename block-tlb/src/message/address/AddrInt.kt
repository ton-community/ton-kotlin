@file:Suppress("NOTHING_TO_INLINE", "PropertyName")

package org.ton.block.message.address

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

import org.ton.bitstring.BitString
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

public inline fun MsgAddressInt(address: String): AddrInt = AddrInt.parse(address)

@OptIn(ExperimentalSerializationApi::class)

@Serializable
public sealed interface AddrInt : MsgAddress {
    public val workchainId: Int
    public val address: BitString

    public companion object : TlbCodec<AddrInt> by MsgAddressIntTlbCombinator {
        @JvmStatic
        public fun tlbCodec(): TlbCombinator<AddrInt> = MsgAddressIntTlbCombinator

        @JvmStatic
        public fun toString(
            address: AddrInt,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            checkAddressStd(address)
            return AddrStd.Companion.toString(address, userFriendly, urlSafe, testOnly, bounceable)
        }

        @JvmStatic
        public fun parse(address: String): AddrInt = AddrStd.Companion.parse(address)

        @JvmStatic
        public fun parseRaw(address: String): AddrInt = AddrStd.Companion.parseRaw(address)

        @JvmStatic
        public fun parseUserFriendly(address: String): AddrInt = AddrStd.Companion.parseUserFriendly(address)

        private fun checkAddressStd(value: AddrInt) {
            contract {
                returns() implies (value is AddrStd)
            }
            require(value is AddrStd) {
                "expected class: ${AddrStd::class} actual: ${value::class}"
            }
        }
    }
}

private object MsgAddressIntTlbCombinator : TlbCombinator<AddrInt>(
    AddrInt::class,
    AddrStd::class to AddrStd.Companion.tlbCodec(),
    AddrVar::class to AddrVar.Companion.tlbCodec()
)
