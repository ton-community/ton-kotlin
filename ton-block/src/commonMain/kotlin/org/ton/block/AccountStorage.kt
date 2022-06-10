package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("account_storage")
@Serializable
data class AccountStorage(
    val last_trans_lt: Long,
    val balance: CurrencyCollection,
    val state: AccountState
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AccountStorage> = AccountStorageTlbConstructor
    }
}

private object AccountStorageTlbConstructor : TlbConstructor<AccountStorage>(
    schema = "account_storage\$_ last_trans_lt:uint64 " +
            "balance:CurrencyCollection state:AccountState = AccountStorage;"
) {
    val currencyCollection by lazy {
        CurrencyCollection.tlbCodec()
    }
    val accountState by lazy {
        AccountState.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountStorage
    ) = cellBuilder {
        storeUInt(value.last_trans_lt, 64)
        storeTlb(currencyCollection, value.balance)
        storeTlb(accountState, value.state)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountStorage = cellSlice {
        val lastTransLt = loadUInt(64).toLong()
        val balance = loadTlb(currencyCollection)
        val state = loadTlb(accountState)
        AccountStorage(lastTransLt, balance, state)
    }
}