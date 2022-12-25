package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

@Serializable
public data class LiteServerShardInfo(
    val id: TonNodeBlockIdExt,
    @SerialName("shardblk")
    val shardBlock: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    val shardProof: BagOfCells,
    @SerialName("shard_descr")
    val shardDescr: BagOfCells
) {
    public companion object : TlCodec<LiteServerShardInfo> by LiteServerShardInfoTlConstructor
}

private object LiteServerShardInfoTlConstructor : TlConstructor<LiteServerShardInfo>(
    schema = "liteServer.shardInfo id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes shard_descr:bytes = liteServer.ShardInfo"
) {
    override fun decode(reader: TlReader): LiteServerShardInfo {
        val id = reader.read(TonNodeBlockIdExt)
        val shardblk = reader.read(TonNodeBlockIdExt)
        val shardProof = reader.readBoc()
        val shardDescr = reader.readBoc()
        return LiteServerShardInfo(id, shardblk, shardProof, shardDescr)
    }

    override fun encode(writer: TlWriter, value: LiteServerShardInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.write(TonNodeBlockIdExt, value.shardBlock)
        writer.writeBoc(value.shardProof)
        writer.writeBoc(value.shardDescr)
    }
}
