package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@SerialName("account_storage")
@Serializable
data class AccountStorage(
    val last_trans_lt: ULong,
    val balance: CurrencyCollection,
    val state: AccountState
) {
    override fun toString(): String = "(account_storage\nlast_trans_lt:$last_trans_lt balance:$balance state:$state)"

    companion object : TlbConstructorProvider<AccountStorage> by AccountStorageTlbConstructor
}

private object AccountStorageTlbConstructor : TlbConstructor<AccountStorage>(
    type = AccountStorage::class,
    schema = "account_storage\$_ last_trans_lt:uint64 " +
            "balance:CurrencyCollection state:AccountState = AccountStorage;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountStorage
    ) = cellBuilder {
        storeUInt64(value.last_trans_lt)
        storeTlb(CurrencyCollection, value.balance)
        storeTlb(AccountState, value.state)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountStorage = cellSlice {
        val lastTransLt = loadUInt64()
        val balance = loadTlb(CurrencyCollection)
        val state = loadTlb(AccountState)
        AccountStorage(lastTransLt, balance, state)
    }
}
