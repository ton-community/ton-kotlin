@file:UseSerializers(Base64ByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.Shard
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.Account
import org.ton.boc.BagOfCells
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import org.ton.tlb.CellRef

@Serializable
public data class LiteServerAccountState(
    val id: TonNodeBlockIdExt,
    @SerialName("shardblk")
    val shardBlock: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    val shardProof: BagOfCells,
    val proof: BagOfCells,
    val state: BagOfCells
) {
    public fun stateAsAccount(): CellRef<Account> = CellRef(state.first(), Account)

    public fun check(blockIdExt: TonNodeBlockIdExt, liteServerAccountId: LiteServerAccountId) {
        check(id != blockIdExt && blockIdExt.seqno != 0.inv()) { "invalid block_id, expected: $blockIdExt, actual: $id" }
        check(shardBlock.isValidFull()) { "invalid shard_blk: $shardBlock" }
        Shard.check(id, shardBlock, shardProof.first())
        // TODO: check shard
    }

    public companion object : TlConstructor<LiteServerAccountState>(
        schema = "liteServer.accountState id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes proof:bytes state:bytes = liteServer.AccountState"
    ) {
        override fun decode(reader: TlReader): LiteServerAccountState {
            val id = reader.read(TonNodeBlockIdExt)
            val shardBlk = reader.read(TonNodeBlockIdExt)
            val shardProof = reader.readBoc()
            val proof = reader.readBoc()
            val state = reader.readBoc()
            return LiteServerAccountState(id, shardBlk, shardProof, proof, state)
        }

        override fun encode(writer: TlWriter, value: LiteServerAccountState) {
            writer.write(TonNodeBlockIdExt, value.id)
            writer.write(TonNodeBlockIdExt, value.shardBlock)
            writer.writeBoc(value.shardProof)
            writer.writeBoc(value.proof)
            writer.writeBoc(value.state)
        }
    }
}
