package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class AccountStorage(
    val lastTransLt: Long,
    val balance: CurrencyCollection,
    val state: AccountState
)