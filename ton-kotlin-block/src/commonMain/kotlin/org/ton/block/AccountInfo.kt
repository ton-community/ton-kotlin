package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("account")
public data class AccountInfo(
    val addr: MsgAddressInt, // addr : MsgAddressInt
    @SerialName("storage_stat") val storageStat: StorageInfo, // storage_stat : StorageInfo
    val storage: AccountStorage // storage : AccountStorage
) : Account {
    public companion object : TlbConstructorProvider<AccountInfo> by AccountInfoTlbConstructor

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("account") {
        field("addr", addr)
        field("storage_stat", storageStat)
        field("storage", storage)
    }

    override fun toString(): String = print().toString()
}

private object AccountInfoTlbConstructor : TlbConstructor<AccountInfo>(
    schema = "account\$1 addr:MsgAddressInt storage_stat:StorageInfo storage:AccountStorage = Account;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountInfo
    ) = cellBuilder {
        storeTlb(MsgAddressInt, value.addr)
        storeTlb(StorageInfo, value.storageStat)
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
