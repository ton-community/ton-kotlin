package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
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
    override fun toString(): String = "account_storage(last_trans_lt:$last_trans_lt balance:$balance state:$state)"

    companion object : TlbCodec<AccountStorage> by AccountStorageTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AccountStorage> = AccountStorageTlbConstructor
    }
}

private object AccountStorageTlbConstructor : TlbConstructor<AccountStorage>(
    schema = "account_storage\$_ last_trans_lt:uint64 " +
            "balance:CurrencyCollection state:AccountState = AccountStorage;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountStorage
    ) = cellBuilder {
        storeUInt(value.last_trans_lt, 64)
        storeTlb(CurrencyCollection, value.balance)
        storeTlb(AccountState, value.state)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountStorage = cellSlice {
        val lastTransLt = loadUInt(64).toLong()
        val balance = loadTlb(CurrencyCollection)
        val state = loadTlb(AccountState)
        AccountStorage(lastTransLt, balance, state)
    }
}