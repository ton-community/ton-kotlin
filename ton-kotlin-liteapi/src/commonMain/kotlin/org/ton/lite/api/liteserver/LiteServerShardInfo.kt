package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.shardInfo")
public data class LiteServerShardInfo(
    val id: TonNodeBlockIdExt,
    @SerialName("shardblk")
    val shardBlock: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    val shardProof: ByteArray,
    @SerialName("shard_descr")
    val shardDescr: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerShardInfo) return false

        if (id != other.id) return false
        if (shardBlock != other.shardBlock) return false
        if (!shardProof.contentEquals(other.shardProof)) return false
        if (!shardDescr.contentEquals(other.shardDescr)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shardBlock.hashCode()
        result = 31 * result + shardProof.contentHashCode()
        result = 31 * result + shardDescr.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerShardInfo> by LiteServerShardInfoTlConstructor
}

private object LiteServerShardInfoTlConstructor : TlConstructor<LiteServerShardInfo>(
    schema = "liteServer.shardInfo id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes shard_descr:bytes = liteServer.ShardInfo"
) {
    override fun decode(reader: TlReader): LiteServerShardInfo {
        val id = reader.read(TonNodeBlockIdExt)
        val shardblk = reader.read(TonNodeBlockIdExt)
        val shardProof = reader.readBytes()
        val shardDescr = reader.readBytes()
        return LiteServerShardInfo(id, shardblk, shardProof, shardDescr)
    }

    override fun encode(writer: TlWriter, value: LiteServerShardInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.write(TonNodeBlockIdExt, value.shardBlock)
        writer.writeBytes(value.shardProof)
        writer.writeBytes(value.shardDescr)
    }
}
