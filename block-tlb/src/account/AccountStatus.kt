package org.ton.kotlin.account

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer

/**
 * Brief account status.
 */
public enum class AccountStatus {
    /**
     * Account exists but has not yet been deployed.
     */
    Uninit,

    /**
     * Account exists but has been frozen.
     */
    Frozen,

    /**
     * Account exists and has been deployed.
     */
    Active,

    /**
     * Account does not exist.
     */
    NotExist;

    public companion object : CellSerializer<AccountStatus> by AccountStatusSerializer
}

private object AccountStatusSerializer : CellSerializer<AccountStatus> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): AccountStatus {
        val tag = slice.loadUInt(2).toInt()
        return when (tag) {
            0b00 -> AccountStatus.Uninit
            0b01 -> AccountStatus.Frozen
            0b10 -> AccountStatus.Active
            0b11 -> AccountStatus.NotExist
            else -> throw IllegalStateException("Unknown account status type: $tag")
        }
    }

    override fun store(
        builder: CellBuilder,
        value: AccountStatus,
        context: CellContext
    ) {
        val tag = when (value) {
            AccountStatus.Uninit -> 0b00u
            AccountStatus.Frozen -> 0b01u
            AccountStatus.Active -> 0b10u
            AccountStatus.NotExist -> 0b11u
        }
        builder.storeUInt(tag, 2)
    }
}