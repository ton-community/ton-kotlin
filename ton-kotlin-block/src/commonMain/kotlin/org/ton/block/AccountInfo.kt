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

@Serializable
@SerialName("account")
data class AccountInfo(
    val addr: MsgAddressInt,
    val storage_stat: StorageInfo,
    val storage: AccountStorage
) : Account {
    companion object : TlbConstructorProvider<AccountInfo> by AccountInfoTlbConstructor

    override fun toString(): String = "(account\naddr:$addr storage_stat:$storage_stat storage:$storage)"
}

private object AccountInfoTlbConstructor : TlbConstructor<AccountInfo>(
    schema = "account\$1 addr:MsgAddressInt storage_stat:StorageInfo storage:AccountStorage = Account;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountInfo
    ) = cellBuilder {
        storeTlb(MsgAddressInt, value.addr)
        storeTlb(StorageInfo, value.storage_stat)
        storeTlb(AccountStorage, value.storage)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountInfo = cellSlice {
        val addr = loadTlb(MsgAddressInt)
        val storageStat = loadTlb(StorageInfo)
        val storage = loadTlb(AccountStorage)
        AccountInfo(addr, storageStat, storage)
    }
}
