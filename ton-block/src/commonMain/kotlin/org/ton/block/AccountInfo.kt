package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("account")
data class AccountInfo(
    val addr: MsgAddressInt,
    val storage_stat: StorageInfo,
    val storage: AccountStorage
) : Account {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AccountInfo> = AccountInfoTlbConstructor
    }
}

private object AccountInfoTlbConstructor : TlbConstructor<AccountInfo>(
    schema = "account\$1 addr:MsgAddressInt storage_stat:StorageInfo storage:AccountStorage = Account;"
) {
    val msgAddressInt by lazy {
        MsgAddressInt.tlbCodec()
    }
    val storageInfo by lazy {
        StorageInfo.tlbCodec()
    }
    val accountStorage by lazy {
        AccountStorage.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountInfo
    ) = cellBuilder {
        storeTlb(msgAddressInt, value.addr)
        storeTlb(storageInfo, value.storage_stat)
        storeTlb(accountStorage, value.storage)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountInfo = cellSlice {
        val addr = loadTlb(msgAddressInt)
        val storageStat = loadTlb(storageInfo)
        val storage = loadTlb(accountStorage)
        AccountInfo(addr, storageStat, storage)
    }
}