package org.ton.tonlib.types

internal sealed interface AccountState

internal data class RawAccountState(
    val code: String,
    val data: String,
    val frozenHash: String
) : AccountState

internal data class WalletV3AccountState(
    val walletId: Long,
    val seqno: Int
) : AccountState

internal data class WalletHighloadV1AccountState(
    val walletId: Long,
    val seqno: Int
) : AccountState

internal data class DnsAccountState(
    val walletId: Long
) : AccountState

internal data class RWalletAccountState(
    val walletId: Long,
    val seqno: Int,
    val unlockedBalance: Long,
    val config: RWalletConfig
) : AccountState

internal data class UninitedAccountState(
    val frozenHash: String
)
