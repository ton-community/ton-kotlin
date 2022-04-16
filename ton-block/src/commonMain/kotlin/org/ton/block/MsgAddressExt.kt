@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.bitstring.BitString

@JsonClassDiscriminator("@type")
@Serializable
sealed interface MsgAddressExt {
    @SerialName("addr_none")
    @Serializable
    object AddrNone : MsgAddressExt

    @SerialName("addr_extern")
    @Serializable
    data class AddrExtern(
        val len: Int,
        val external_address: BitString
    ) : MsgAddressExt
}