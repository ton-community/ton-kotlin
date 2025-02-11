package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.TlbConstructor

/**
 * Existing account data.
 */
public data class Account(
    /**
     * Account address.
     */
    val address: MsgAddressInt,

    /**
     * Storage statistics.
     */
    val storageStat: StorageInfo,

    /**
     * Logical time after the last transaction execution.
     */
    val lastTransLt: Long,

    /**
     * Account balance for all currencies.
     */
    val balance: CurrencyCollection,

    /**
     * Account state.
     */
    val state: AccountState
) {
    public companion object : TlbCodec<Account?> by AccountInfoTlbConstructor.asNullable()

    @Deprecated("Use fields lastTransLt, balance, state instead")
    val storage: AccountStorage // storage : AccountStorage
        get() = AccountStorage(lastTransLt.toULong(), balance, state)

    @Deprecated("Use address instead", ReplaceWith("address"))
    val addr: MsgAddressInt
        get() = address

    val isActive: Boolean get() = storage.state is AccountActive
    val isFrozen: Boolean get() = storage.state is AccountFrozen
    val isUninit: Boolean get() = storage.state is AccountUninit
}

public val Account?.balance: CurrencyCollection
    get() = this?.balance ?: CurrencyCollection.ZERO

public val Account?.accountLastTransLt: Long
    get() = this?.lastTransLt ?: 0

public val Account?.status: AccountStatus
    get() = this?.state?.status ?: AccountStatus.NONEXIST

private object AccountInfoTlbConstructor : TlbConstructor<Account>(
    schema = "account\$1 addr:MsgAddressInt storage_stat:StorageInfo storage:AccountStorage = Account;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Account
    ) = cellBuilder {
        storeTlb(MsgAddressInt, value.addr)
        storeTlb(StorageInfo, value.storageStat)
        storeULong(value.lastTransLt.toULong())
        storeTlb(CurrencyCollection, value.balance)
        storeTlb(AccountState, value.state)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Account = cellSlice {
        val addr = loadTlb(MsgAddressInt)
        val storageStat = loadTlb(StorageInfo)
        val lastTransLt = loadULong().toLong()
        val balance = loadTlb(CurrencyCollection)
        val state = loadTlb(AccountState)
        Account(addr, storageStat, lastTransLt, balance, state)
    }
}
