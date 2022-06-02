package org.ton.block

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressInt : MsgAddress {
    val workchainId: Int

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<MsgAddressInt> = MsgAddressIntTlbCombinator()

        @JvmStatic
        fun toString(
            address: MsgAddressInt,
            userFriendly: Boolean = true,
            urlSafe: Boolean = true,
            testOnly: Boolean = false,
            bounceable: Boolean = true
        ): String {
            checkAddressStd(address)
            return MsgAddressIntStd.toString(address, userFriendly, urlSafe, testOnly, bounceable)
        }

        @JvmStatic
        fun parse(address: String): MsgAddressInt = MsgAddressIntStd.parse(address)

        @JvmStatic
        fun parseRaw(address: String): MsgAddressInt = MsgAddressIntStd.parseRaw(address)

        @JvmStatic
        fun parseUserFriendly(address: String): MsgAddressInt = MsgAddressIntStd.parseUserFriendly(address)

        @OptIn(ExperimentalContracts::class)
        private fun checkAddressStd(value: MsgAddressInt) {
            contract {
                returns() implies (value is MsgAddressIntStd)
            }
            require(value is MsgAddressIntStd) {
                "expected class: ${MsgAddressIntStd::class} actual: ${value::class}"
            }
        }
    }
}

internal class MsgAddressIntTlbCombinator : TlbCombinator<MsgAddressInt>() {
    private val addrStdConstructor by lazy {
        MsgAddressIntStd.tlbCodec()
    }
    private val addrVarConstructor by lazy {
        MsgAddressIntVar.tlbCodec()
    }

    override val constructors: List<TlbConstructor<out MsgAddressInt>> by lazy {
        listOf(addrStdConstructor, addrVarConstructor)
    }

    override fun getConstructor(value: MsgAddressInt): TlbConstructor<out MsgAddressInt> = when (value) {
        is MsgAddressIntStd -> addrStdConstructor
        is MsgAddressIntVar -> addrVarConstructor
    }
}

