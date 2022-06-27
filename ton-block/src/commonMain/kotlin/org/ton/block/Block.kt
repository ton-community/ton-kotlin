package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
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
    companion object TlbCombinator : org.ton.tlb.TlbCombinator<Block>() {
        override val constructors: List<org.ton.tlb.TlbConstructor<out Block>> =
            listOf(TlbConstructor)

        override fun getConstructor(value: Block): org.ton.tlb.TlbConstructor<out Block> =
            TlbConstructor
    }

    object TlbConstructor : org.ton.tlb.TlbConstructor<Block>(
        schema = "block#11ef55aa global_id:int32 " +
                "info:^BlockInfo value_flow:^ValueFlow " +
                "state_update:^(MERKLE_UPDATE ShardState) " +
                "extra:^BlockExtra = Block;"
    ) {
        private val merkleUpdate by lazy { MerkleUpdate.tlbCodec(ShardState.tlbCodec()) }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Block
        ) = cellBuilder {
            storeInt(value.global_id, 32)
            storeRef { storeTlb(BlockInfo, value.info) }
            storeRef { storeTlb(ValueFlow, value.value_flow) }
            storeRef { storeTlb(merkleUpdate, value.state_update) }
            storeRef { storeTlb(BlockExtra, value.extra) }
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Block = cellSlice {
            val globalId = loadInt(32).toInt()
            val info = loadRef { loadTlb(BlockInfo) }
            val valueFlow = loadRef { loadTlb(ValueFlow) }
            val stateUpdate = loadRef { loadTlb(merkleUpdate) }
            val extra = loadRef { loadTlb(BlockExtra) }
            Block(globalId, info, valueFlow, stateUpdate, extra)
        }
    }
}