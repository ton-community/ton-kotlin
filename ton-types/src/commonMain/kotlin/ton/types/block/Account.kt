package ton.types.block

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val addr: MsgAddressInt,
    val storageStat: StorageInfo,
    val storage: AccountStorage
)