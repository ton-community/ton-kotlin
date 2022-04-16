@file:Suppress("OPT_IN_USAGE")

package ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Account {
    @SerialName("account")
    @Serializable
    data class AccountImpl(
        val addr: MsgAddressInt,
        val storage_stat: StorageInfo,
        val storage: AccountStorage
    ) : Account

    @SerialName("account_none")
    @Serializable
    object AccountNone : Account
}