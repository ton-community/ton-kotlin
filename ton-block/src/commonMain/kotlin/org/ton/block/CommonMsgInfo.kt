@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface CommonMsgInfo {
    @SerialName("int_msg_info")
    data class IntMsgInfo(
        @SerialName("ihr_disabled")
        val ihrDisabled: Boolean,
        val bounce: Boolean,
        val bounced: Boolean,
        val src: MsgAddressInt,
        val dest: MsgAddressInt,
        val value: CurrencyCollection,
        @SerialName("ihr_fee")
        val ihrFee: Coins,
        @SerialName("fwd_fee")
        val fwdFee: Coins,
        @SerialName("created_lt")
        val createdLt: Long,
        @SerialName("created_at")
        val createdAt: Int
    ) : CommonMsgInfo

    @SerialName("ext_in_msg_info")
    data class ExtInMsgInfo(
        val src: MsgAddressExt,
        val dest: MsgAddressInt,
        @SerialName("import_fee")
        val importFee: Coins = Coins(0)
    ) : CommonMsgInfo {
        constructor(
            dest: MsgAddressInt,
            importFee: Coins = Coins(0)
        ) : this(MsgAddressExt.AddrNone, dest, importFee)
    }

    @SerialName("ext_out_msg_info")
    data class ExtOutMsgInfo(
        val src: MsgAddressInt,
        val dest: MsgAddressExt,
        @SerialName("created_lt")
        val createdLt: Long,
        @SerialName("created_at")
        val createdAt: Int
    ) : CommonMsgInfo
}
