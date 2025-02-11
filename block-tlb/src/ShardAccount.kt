package org.ton.block

import kotlinx.io.bytestring.ByteString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.kotlin.cell.CellContext
import org.ton.tlb.CellRef
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider

/**
 * Shard accounts entry.
 */
public data class ShardAccount(
    /**
     * Optional reference to account state.
     */
    val account: CellRef<Account?>,

    /**
     * The exact hash of the last transaction.
     */
    val lastTransHash: ByteString,

    /**
     * The exact logical time of the last transaction.
     */
    val lastTransLt: Long
) : TlbObject {

    /**
     * Load account data from cell.
     */
    public fun loadAccount(context: CellContext = CellContext.EMPTY): Account? {
        return account.load(context)
    }

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("account_descr") {
        field("account", account)
        field("last_trans_hash", lastTransHash)
        field("last_trans_lt", lastTransLt)
    }

    override fun toString(): String = print().toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ShardAccount
        if (lastTransLt != other.lastTransLt) return false
        if (account != other.account) return false
        if (lastTransHash != other.lastTransHash) return false
        return true
    }

    override fun hashCode(): Int {
        var result = lastTransLt.hashCode()
        result = 31 * result + account.hashCode()
        result = 31 * result + lastTransHash.hashCode()
        return result
    }

    public companion object : TlbConstructorProvider<ShardAccount> by ShardAccountTlbConstructor
}

private object ShardAccountTlbConstructor : TlbConstructor<ShardAccount>(
    schema = "account_descr\$_ account:^Account last_trans_hash:bits256 last_trans_lt:uint64 = ShardAccount;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardAccount
    ) = cellBuilder {
        storeRef(value.account.cell)
        storeBytes(value.lastTransHash.toByteArray())
        storeULong(value.lastTransLt.toULong())
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardAccount = cellSlice {
        val account = CellRef(loadRef(), Account)
        val lastTransHash = loadByteString(32)
        val lastTransLt = loadULong().toLong()
        ShardAccount(account, lastTransHash, lastTransLt)
    }
}
