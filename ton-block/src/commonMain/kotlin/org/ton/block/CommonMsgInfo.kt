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
        val ihr_disabled: Boolean,
        val bounce: Boolean,
        val bounced: Boolean,
        val src: MsgAddressInt,
        val dest: MsgAddressInt,
        val value: CurrencyCollection,
        val ihr_fee: Grams,
        val fwd_fee: Grams,
        val created_lt: Long,
        val created_at: Long
    ) : CommonMsgInfo

    @SerialName("ext_in_msg_info")
    data class ExtInMsgInfo(
        val src: MsgAddressExt,
        val dest: MsgAddressInt,
        val import_fee: Grams
    ) : CommonMsgInfo

    @SerialName("ext_out_msg_info")
    data class ExtOutMsgInfo(
        val src: MsgAddressInt,
        val dest: MsgAddressExt,
        val created_lt: Long,
        val created_at: Long
    )
}