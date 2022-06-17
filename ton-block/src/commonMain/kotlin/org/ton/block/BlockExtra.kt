package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.AugDictionary
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.UIntTlbConstructor
import org.ton.tlb.storeTlb

@SerialName("block_extra")
@Serializable
data class BlockExtra(
    val in_msg_descr: AugDictionary<InMsg, ImportFees>,
    val out_msg_descr: AugDictionary<OutMsg, CurrencyCollection>,
    val account_blocks: AugDictionary<AccountBlock, CurrencyCollection>,
    val rand_seed: BitString,
    val created_by: BitString,
    val custom: Maybe<McBlockExtra>
) {
    init {
        require(rand_seed.size == 256) { "expected: rand_seed.size == 256, actual: ${rand_seed.size}" }
        require(created_by.size == 256) { "expected: created_by.size == 256, actual: ${created_by.size}" }
    }
}

private object BlockExtraTlbConstructor : TlbConstructor<BlockExtra>(
    schema = "block_extra in_msg_descr:^InMsgDescr\n" +
            "  out_msg_descr:^OutMsgDescr\n" +
            "  account_blocks:^ShardAccountBlocks\n" +
            "  rand_seed:bits256\n" +
            "  created_by:bits256\n" +
            "  custom:(Maybe ^McBlockExtra) = BlockExtra;"
) {
    val inMsgDescr by lazy { AugDictionary.tlbCodec(256, InMsg.tlbCodec(), ImportFees.tlbCodec()) }
    val outMsgDescr by lazy { AugDictionary.tlbCodec(356, OutMsg.tlbCodec(), CurrencyCollection.long(64)) }
    val shardAccountBlock by lazy {
        AugDictionary.tlbCodec(
            256,
            AccountBlock.tlbCodec(),
            CurrencyCollection.tlbCodec()
        )
    }
    val maybeMcBlockExtra by lazy { Maybe.tlbCodec(McBlockExtra.tlbCodec()) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: BlockExtra
    ) = cellBuilder {
        storeRef { storeTlb(inMsgDescr, value.in_msg_descr) }
        storeRef { storeTlb(outMsgDescr, value.out_msg_descr) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): BlockExtra = cellSlice {
        TODO("Not yet implemented")
    }
}