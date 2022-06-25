@file:Suppress("NOTHING_TO_INLINE", "PropertyName")

package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

inline fun MsgAddressInt(address: String): MsgAddressInt = MsgAddressInt.parse(address)

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressInt : MsgAddress {
    val workchainId: Int

    companion object {
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
        fun parseUserFriendly(address: String): MsgAddressInt = AddrStd.parseUserFriendly(address)

        @OptIn(ExperimentalContracts::class)
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

private object MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>() {
    private val addrStdConstructor by lazy {
        AddrStd.tlbCodec()
    }
    private val addrVarConstructor by lazy {
        AddrVar.tlbCodec()
    }

    override val constructors: List<TlbConstructor<out MsgAddressInt>> by lazy {
        listOf(addrStdConstructor, addrVarConstructor)
    }

    override fun getConstructor(value: MsgAddressInt): TlbConstructor<out MsgAddressInt> = when (value) {
        is AddrStd -> addrStdConstructor
        is AddrVar -> addrVarConstructor
    }
}

