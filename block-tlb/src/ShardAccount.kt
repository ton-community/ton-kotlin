package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*
import org.ton.tlb.providers.TlbConstructorProvider

@Serializable
@SerialName("account_descr")
public data class ShardAccount(
    val account: CellRef<Account>,
    @SerialName("last_trans_hash") val lastTransHash: BitString,
    @SerialName("last_trans_lt") val lastTransLt: ULong
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("account_descr") {
        field("account", account)
        field("last_trans_hash", lastTransHash)
        field("last_trans_lt", lastTransLt)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<ShardAccount> by ShardAccountTlbConstructor
}

private object ShardAccountTlbConstructor : TlbConstructor<ShardAccount>(
    schema = "account_descr\$_ account:^Account last_trans_hash:bits256 last_trans_lt:uint64 = ShardAccount;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardAccount
    ) = cellBuilder {
        storeRef(Account, value.account)
        storeBits(value.lastTransHash)
        storeUInt64(value.lastTransLt)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardAccount = cellSlice {
        val account = loadRef(Account)
        val lastTransHash = loadBits(256)
        val lastTransLt = loadULong()
        ShardAccount(account, lastTransHash, lastTransLt)
    }
}
