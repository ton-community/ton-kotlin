package org.ton.kotlin.account

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.currency.CurrencyCollection
import org.ton.kotlin.message.address.IntAddr

/**
 * Existing account data.
 */
public data class Account(
    /**
     * Account address.
     */
    val address: IntAddr,

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
//    public object Serializer : CellSerializer<Account> {
//        override fun store(builder: CellBuilder, value: Account, context: CellContext) {
//            builder.storeTlb(IntAddr, value.address)
//            builder.storeTlb(StorageInfo.Tlb, value.storageStat)
//            builder.storeULong(value.lastTransLt.toULong())
//            builder.storeTlb(CurrencyCollection.Tlb, value.balance)
//            builder.storeTlb(AccountState.Tlb, value.state)
//        }
//
//        override fun loadTlb(
//            cellSlice: CellSlice
//        ): Account = cellSlice {
//            val addr = loadTlb(IntAddr.Companion)
//            val storageStat = loadTlb(StorageInfo.Tlb)
//            val lastTransLt = loadULong().toLong()
//            val balance = loadTlb(CurrencyCollection.Tlb)
//            val state = loadTlb(AccountState.Tlb)
//            Account(addr, storageStat, lastTransLt, balance, state)
//        }
//    }
}

public val Account?.balance: CurrencyCollection
    get() = this?.balance ?: CurrencyCollection.ZERO

public val Account?.accountLastTransLt: Long
    get() = this?.lastTransLt ?: 0

public val Account?.status: AccountStatus
    get() = this?.state?.status ?: AccountStatus.NotExist

private object AccountSerializer : CellSerializer<Account> {
    override fun load(
        slice: CellSlice,
        context: CellContext
    ): Account {
        slice.load(IntAddr, context)
        slice.load(StorageInfo, context)
    }

    override fun store(
        builder: CellBuilder,
        value: Account,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}

