@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Account {
    @SerialName("account")
    @Serializable
    data class AccountImpl(
            val addr: org.ton.block.MsgAddressInt,
            val storage_stat: org.ton.block.StorageInfo,
            val storage: org.ton.block.AccountStorage
    ) : org.ton.block.Account

    @SerialName("account_none")
    @Serializable
    object AccountNone : org.ton.block.Account
}