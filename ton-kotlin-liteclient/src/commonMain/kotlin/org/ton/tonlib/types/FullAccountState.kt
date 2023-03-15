package org.ton.tonlib.types

internal data class FullAccountState(
    val address: AccountAddress,
    val balance: Long,
    val lastTransactionId: InternalTransactionId,
    val blockId: TonBlockIdExt,
    val syncTime: Long,
    val accountState: AccountState,
    val revision: Int
)
