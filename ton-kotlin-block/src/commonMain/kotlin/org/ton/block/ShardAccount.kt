package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("account_descr")
data class ShardAccount(
    val account: Account,
    val last_trans_hash: BitString,
    val last_trans_lt: ULong
) {
    init {
        require(last_trans_hash.size == 256) { "required: last_tans_hash.size == 256, actual: ${last_trans_hash.size}" }
    }

    companion object : TlbConstructorProvider<ShardAccount> by ShardAccountTlbConstructor
}

private object ShardAccountTlbConstructor : TlbConstructor<ShardAccount>(
    schema = "account_descr\$_ account:^Account last_trans_hash:bits256 last_trans_lt:uint64 = ShardAccount;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardAccount
    ) = cellBuilder {
        storeTlb(Account, value.account)
        storeBits(value.last_trans_hash)
        storeUInt64(value.last_trans_lt)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardAccount = cellSlice {
        val account = loadTlb(Account)
        val lastTransHash = loadBits(256)
        val lastTransLt = loadUInt64()
        ShardAccount(account, lastTransHash, lastTransLt)
    }
}
