package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("account_storage")
@Serializable
data class AccountStorage(
    val last_trans_lt: Long,
    val balance: CurrencyCollection,
    val state: org.ton.block.AccountState
)