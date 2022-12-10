package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.hashmap.AugDictionary
import org.ton.tlb.*

@SerialName("block_extra")
@Serializable
data class BlockExtra(
    val in_msg_descr: CellRef<AugDictionary<InMsg, ImportFees>>,
    val out_msg_descr: CellRef<AugDictionary<OutMsg, CurrencyCollection>>,
    val account_blocks: CellRef<AugDictionary<AccountBlock, CurrencyCollection>>,
    val rand_seed: BitString,
    val created_by: BitString,
    val custom: Maybe<CellRef<McBlockExtra>>
) {
    init {
        require(rand_seed.size == 256) { "expected: rand_seed.size == 256, actual: ${rand_seed.size}" }
        require(created_by.size == 256) { "expected: created_by.size == 256, actual: ${created_by.size}" }
    }

    companion object : TlbCodec<BlockExtra> by BlockExtraTlbConstructor.asTlbCombinator()
}

private object BlockExtraTlbConstructor : TlbConstructor<BlockExtra>(
    schema = "block_extra#4a33f6fd in_msg_descr:^InMsgDescr\n" +
            "  out_msg_descr:^OutMsgDescr\n" +
            "  account_blocks:^ShardAccountBlocks\n" +
            "  rand_seed:bits256\n" +
            "  created_by:bits256\n" +
            "  custom:(Maybe ^McBlockExtra) = BlockExtra;"
) {
    val inMsgDescr = CellRef.tlbCodec(AugDictionary.tlbCodec(256, InMsg, ImportFees))
    val outMsgDescr = CellRef.tlbCodec(AugDictionary.tlbCodec(256, OutMsg, CurrencyCollection))
    val shardAccountBlock = CellRef.tlbCodec(
        AugDictionary.tlbCodec(
            256,
            AccountBlock,
            CurrencyCollection
        )
    )
    val maybeMcBlockExtra = Maybe.tlbCodec(CellRef.tlbCodec(McBlockExtra))

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BlockExtra
    ) = cellBuilder {
        storeTlb(inMsgDescr, value.in_msg_descr)
        storeTlb(outMsgDescr, value.out_msg_descr)
        storeTlb(shardAccountBlock, value.account_blocks)
        storeBits(value.rand_seed)
        storeBits(value.created_by)
        storeTlb(maybeMcBlockExtra, value.custom)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BlockExtra = cellSlice {
        val inMsgDescr = cellSlice.loadTlb(inMsgDescr)
        val outMsgDescr = cellSlice.loadTlb(outMsgDescr)
        val accountBlocks = loadTlb(shardAccountBlock)
        val randSeed = loadBits(256)
        val createdBy = loadBits(256)
        val custom = loadTlb(maybeMcBlockExtra)
        BlockExtra(inMsgDescr, outMsgDescr, accountBlocks, randSeed, createdBy, custom)
    }
}
