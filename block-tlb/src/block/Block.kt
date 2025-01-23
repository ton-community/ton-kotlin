package org.ton.block.block

import org.ton.block.MerkleUpdate
import org.ton.block.ValueFlow
import org.ton.block.shard.ShardState
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.CellRef
import org.ton.tlb.TlbConstructor
import org.ton.tlb.asRef

/**
 * Shard block.
 */
public data class Block(
    /**
     * Global network id.
     */
    val globalId: Int,

    /**
     * Block info.
     */
    val info: CellRef<BlockInfo>,

    /**
     * Currency flow info.
     */
    val valueFlow: CellRef<ValueFlow>,

    /**
     * Merkle update for the shard state.
     */
    val stateUpdate: CellRef<MerkleUpdate<ShardState>>,

    /**
     * Block content.
     */
    val extra: CellRef<BlockExtra>
) {
    /**
     * Tries to load block info.
     */
    public fun loadInfo(): Result<BlockInfo> = runCatching {
        info.value
    }

    /**
     * Tries to load tokens flow info.
     */
    public fun loadValueFlow(): Result<ValueFlow> = runCatching {
        valueFlow.value
    }

    /**
     * Tries to load state update.
     */
    public fun loadStateUpdate(): Result<MerkleUpdate<ShardState>> = runCatching {
        stateUpdate.value
    }

    /**
     * Tries to load block content.
     */
    public fun loadExtra(): Result<BlockExtra> = runCatching {
        extra.value
    }

    public object Tlb : TlbConstructor<Block>(
        schema = "block#11ef55aa global_id:int32 " +
                "info:^BlockInfo value_flow:^ValueFlow " +
                "state_update:^(MERKLE_UPDATE ShardState) " +
                "extra:^BlockExtra = Block;"
    ) {
        private val merkleUpdate = MerkleUpdate.Companion.tlbCodec(ShardState.Companion)

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Block
        ): Unit = cellBuilder {
            storeInt(value.globalId, 32)
            storeRef(value.info.toCell(BlockInfo.Companion))
            storeRef(value.valueFlow.toCell(ValueFlow.Companion))
            storeRef(value.stateUpdate.toCell(merkleUpdate))
            storeRef(value.extra.toCell(BlockExtra.Companion))
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Block = cellSlice {
            val globalId = loadInt(32)
            val info = loadRef().asRef(BlockInfo.Companion)
            val valueFlow = loadRef().asRef(ValueFlow.Companion)
            val stateUpdate = loadRef().asRef(merkleUpdate)
            val extra = loadRef().asRef(BlockExtra.Companion)
            Block(globalId, info, valueFlow, stateUpdate, extra)
        }
    }
}

