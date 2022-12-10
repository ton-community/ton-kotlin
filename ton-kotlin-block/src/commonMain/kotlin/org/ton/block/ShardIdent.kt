package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.asTlbCombinator

@SerialName("shard_ident")
@Serializable
data class ShardIdent(
    val shard_pfx_bits: Int,
    val workchain_id: Int,
    val shard_prefix: ULong
) {
    init {
        require(shard_pfx_bits <= 60) { "expected: shard_pfx_bits <= 60, actual: $shard_pfx_bits" }
    }

    companion object : TlbCodec<ShardIdent> by ShardIdentTlbConstructor.asTlbCombinator()
}

private object ShardIdentTlbConstructor : TlbConstructor<ShardIdent>(
    schema = "shard_ident\$00 shard_pfx_bits:(#<= 60) " +
            "workchain_id:int32 shard_prefix:uint64 = ShardIdent;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: ShardIdent
    ) = cellBuilder {
        storeUIntLeq(value.shard_pfx_bits, 60)
        storeInt(value.workchain_id, 32)
        storeUInt64(value.shard_prefix)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): ShardIdent = cellSlice {
        val shardPfxBits = loadUIntLeq(60).toInt()
        val workchainId = loadInt(32).toInt()
        val shardPrefix = loadUInt64()
        ShardIdent(shardPfxBits, workchainId, shardPrefix)
    }
}
