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
    val info: BlockInfo by infoCell
    val valueFlow: ValueFlow by valueFlowCell
    val stateUpdate: MerkleUpdate<ShardState> by stateUpdateCell
    val extra: BlockExtra by extraCell

    public companion object : TlbCombinatorProvider<Block> by TlbConstructor.asTlbCombinator()

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
            storeRef(value.infoCell.toCell(BlockInfo))
            storeRef(value.valueFlowCell.toCell(ValueFlow))
            storeRef(value.stateUpdateCell.toCell(merkleUpdate))
            storeRef(value.extraCell.toCell(BlockExtra))
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
