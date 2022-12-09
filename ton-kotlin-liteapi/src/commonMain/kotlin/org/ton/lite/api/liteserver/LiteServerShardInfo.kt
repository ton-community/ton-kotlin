package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerShardInfo(
    val id: TonNodeBlockIdExt,
    val shardblk: TonNodeBlockIdExt,
    val shard_proof: ByteArray,
    val shard_descr: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerShardInfo

        if (id != other.id) return false
        if (shardblk != other.shardblk) return false
        if (!shard_proof.contentEquals(other.shard_proof)) return false
        if (!shard_descr.contentEquals(other.shard_descr)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shardblk.hashCode()
        result = 31 * result + shard_proof.contentHashCode()
        result = 31 * result + shard_descr.contentHashCode()
        return result
    }

    companion object : TlCodec<LiteServerShardInfo> by LiteServerShardInfoTlConstructor
}

private object LiteServerShardInfoTlConstructor : TlConstructor<LiteServerShardInfo>(
    schema = "liteServer.shardInfo id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes shard_descr:bytes = liteServer.ShardInfo"
) {
    override fun decode(input: Input): LiteServerShardInfo {
        val id = input.readTl(TonNodeBlockIdExt)
        val shardblk = input.readTl(TonNodeBlockIdExt)
        val shardProof = input.readBytesTl()
        val shardDescr = input.readBytesTl()
        return LiteServerShardInfo(id, shardblk, shardProof, shardDescr)
    }

    override fun encode(output: Output, value: LiteServerShardInfo) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeTl(TonNodeBlockIdExt, value.shardblk)
        output.writeBytesTl(value.shard_proof)
        output.writeBytesTl(value.shard_descr)
    }
}
