@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString

@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressExt : MsgAddress {
    @SerialName("addr_none")
    @Serializable
    object AddrNone : MsgAddressExt

    @SerialName("addr_extern")
    @Serializable
    data class AddrExtern(
        val len: Int,
        @SerialName("external_address")
        val externalAddress: BitString
    ) : MsgAddressExt {
        init {
            require(len == externalAddress.size)
        }

        constructor(externalAddress: BitString) : this(externalAddress.size, externalAddress)
    }

    companion object {
        @JvmStatic
        fun of(externalAddress: BitString? = null): MsgAddressExt = MsgAddressExt(externalAddress)
    }
}

fun MsgAddressExt(externalAddress: BitString? = null): MsgAddressExt =
    if (externalAddress == null || externalAddress.isEmpty()) {
        MsgAddressExt.AddrNone
    } else {
        MsgAddressExt.AddrExtern(externalAddress)
    }
