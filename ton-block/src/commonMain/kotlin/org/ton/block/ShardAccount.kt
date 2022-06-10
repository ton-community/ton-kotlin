package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("account_descr")
data class ShardAccount(
    val account: Account,
    val last_tans_hash: BitString,
    val last_trans_lt: Long
) {
    init {
        require(last_tans_hash.size == 256) { "required: last_tans_hash.size == 256, actual: ${last_tans_hash.size}" }
    }
}

private object ShardAccountTlbConstructor : TlbConstructor<ShardAccount>(
    schema = "account_descr\$_ account:^Account last_trans_hash:bits256 last_trans_lt:uint64 = ShardAccount;"
) {
    val account by lazy { Account.tlbCodec() }

    override fun storeTlb(cellBuilder: CellBuilder, value: ShardAccount) {
        TODO("Not yet implemented")
    }

    override fun loadTlb(cellSlice: CellSlice): ShardAccount {
        TODO("Not yet implemented")
    }
}