package org.ton.kotlin.block

import org.ton.kotlin.cell.CellBuilder
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellRef
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.cell.merkle.MerkleUpdate
import org.ton.kotlin.cell.serialization.CellSerializer
import org.ton.kotlin.shard.ShardState

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
    public fun loadInfo(context: CellContext = CellContext.EMPTY): Result<BlockInfo> = runCatching {
        info.load(context)
    }

    /**
     * Tries to load tokens flow info.
     */
    public fun loadValueFlow(context: CellContext = CellContext.EMPTY): Result<ValueFlow> = runCatching {
        valueFlow.load(context)
    }

    /**
     * Tries to load state update.
     */
    public fun loadStateUpdate(context: CellContext = CellContext.EMPTY): Result<MerkleUpdate<ShardState>> =
        runCatching {
            stateUpdate.load(context)
        }

    /**
     * Tries to load block content.
     */
    public fun loadExtra(context: CellContext = CellContext.EMPTY): Result<BlockExtra> = runCatching {
        extra.load(context)
    }

    public companion object : CellSerializer<Block> by BlockSerializer
}

private object BlockSerializer : CellSerializer<Block> {
    private const val TAG = 0x11ef55aa

    override fun load(slice: CellSlice, context: CellContext): Block {
        val tag = slice.loadInt()
        require(tag == TAG) { "Invalid block tag: ${tag.toHexString()}" }
        slice.loadInt()
        CellRef(slice.loadRef(), BlockInfo)
        CellRef(slice.loadRef(), ValueFlow)
        TODO("Not yet implemented")
    }

    override fun store(
        builder: CellBuilder,
        value: Block,
        context: CellContext
    ) {
        TODO("Not yet implemented")
    }

}

/*
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
 */
