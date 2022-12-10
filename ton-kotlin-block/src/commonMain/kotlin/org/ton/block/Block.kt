package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.*
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider

@SerialName("block")
@Serializable
data class Block(
    val global_id: Int,
    val infoCell: CellRef<BlockInfo>,
    val valueFlowCell: CellRef<ValueFlow>,
    val stateUpdateCell: CellRef<MerkleUpdate<ShardState>>,
    val extraCell: CellRef<BlockExtra>
) {
    val info by infoCell
    val valueFlow by valueFlowCell
    val stateUpdate by stateUpdateCell
    val extra by extraCell

    companion object : TlbCombinatorProvider<Block> by TlbConstructor.asTlbCombinator()

    private object TlbConstructor : org.ton.tlb.TlbConstructor<Block>(
        schema = "block#11ef55aa global_id:int32 " +
                "info:^BlockInfo value_flow:^ValueFlow " +
                "state_update:^(MERKLE_UPDATE ShardState) " +
                "extra:^BlockExtra = Block;"
    ) {
        private val merkleUpdate = MerkleUpdate.tlbCodec(ShardState)

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: Block
        ) = cellBuilder {
            storeInt(value.global_id, 32)
            storeRef(value.infoCell.cell)
            storeRef(value.valueFlowCell.cell)
            storeRef(value.stateUpdateCell.cell)
            storeRef(value.extraCell.cell)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): Block = cellSlice {
            val globalId = loadInt(32).toInt()
            val info = loadRef().asRef(BlockInfo)
            val valueFlow = loadRef().asRef(ValueFlow)
            val stateUpdate = loadRef().asRef(merkleUpdate)
            val extra = loadRef().asRef(BlockExtra)
            Block(globalId, info, valueFlow, stateUpdate, extra)
        }
    }
}
