package org.ton.block.account

import org.ton.block.currency.CurrencyCollection
import org.ton.block.message.address.AddrInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

/**
 * Existing account data.
 */
public data class Account(
    /**
     * Account address.
     */
    val address: AddrInt,

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
    public object Tlb : TlbCodec<Account> {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Account
        ): Unit = cellBuilder {
            storeTlb(AddrInt, value.address)
            storeTlb(StorageInfo.Tlb, value.storageStat)
            storeUInt64(value.lastTransLt.toULong())
            storeTlb(CurrencyCollection.Tlb, value.balance)
            storeTlb(AccountState.Tlb, value.state)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Account = cellSlice {
            val addr = loadTlb(AddrInt.Companion)
            val storageStat = loadTlb(StorageInfo.Tlb)
            val lastTransLt = loadULong().toLong()
            val balance = loadTlb(CurrencyCollection.Tlb)
            val state = loadTlb(AccountState.Tlb)
            Account(addr, storageStat, lastTransLt, balance, state)
        }
    }
}

public val Account?.balance: CurrencyCollection
    get() = this?.balance ?: CurrencyCollection.ZERO

public val Account?.accountLastTransLt: Long
    get() = this?.lastTransLt ?: 0

public val Account?.status: AccountStatus
    get() = this?.state?.status ?: AccountStatus.NotExist

