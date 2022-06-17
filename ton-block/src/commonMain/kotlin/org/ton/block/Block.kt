package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@SerialName("block")
@Serializable
data class Block(
    val global_id: Int,
    val info: BlockInfo,
    val value_flow: ValueFlow,
    val state_update: MerkleUpdate<ShardState>,
    val extra: BlockExtra
) {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<Block> = BlockTlbConstructor
    }
}

private object BlockTlbConstructor : TlbConstructor<Block>(
    schema = "block#11ef55aa global_id:int32 " +
            "info:^BlockInfo value_flow:^ValueFlow " +
            "state_update:^(MERKLE_UPDATE ShardState) " +
            "extra:^BlockExtra = Block;"
) {
    val blockInfo by lazy { BlockInfo.tlbCodec() }
    val valueFlow by lazy { ValueFlow.tlbCodec() }
    val merkleUpdate by lazy { MerkleUpdate.tlbCodec(ShardState.tlbCodec()) }
    val blockExtra by lazy { BlockExtra.tlbCodec() }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: Block
    ) = cellBuilder {
        storeInt(value.global_id, 32)
        storeRef { storeTlb(blockInfo, value.info) }
        storeRef { storeTlb(valueFlow, value.value_flow) }
        storeRef { storeTlb(merkleUpdate, value.state_update) }
        storeRef { storeTlb(blockExtra, value.extra) }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): Block = cellSlice {
        val globalId = loadInt(32).toInt()
        val info = loadRef { loadTlb(blockInfo) }
        val valueFlow = loadRef { loadTlb(valueFlow) }
        val stateUpdate = loadRef { loadTlb(merkleUpdate) }
        val extra = loadRef { loadTlb(blockExtra) }
        Block(globalId, info, valueFlow, stateUpdate, extra)
    }
}