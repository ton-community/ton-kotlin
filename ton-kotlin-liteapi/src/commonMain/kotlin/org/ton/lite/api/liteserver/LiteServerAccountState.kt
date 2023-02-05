@file:UseSerializers(Base64ByteArraySerializer::class)

package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.tl.*

@Serializable
@SerialName("liteServer.accountState")
public data class LiteServerAccountState(
    val id: TonNodeBlockIdExt,
    @SerialName("shardblk")
    val shardBlock: TonNodeBlockIdExt,
    @SerialName("shard_proof")
    val shardProof: ByteArray,
    val proof: ByteArray,
    val state: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerAccountState) return false

        if (id != other.id) return false
        if (shardBlock != other.shardBlock) return false
        if (!shardProof.contentEquals(other.shardProof)) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!state.contentEquals(other.state)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shardBlock.hashCode()
        result = 31 * result + shardProof.contentHashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + state.contentHashCode()
        return result
    }

    public companion object : TlConstructor<LiteServerAccountState>(
        schema = "liteServer.accountState id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes proof:bytes state:bytes = liteServer.AccountState"
    ) {
        override fun decode(reader: TlReader): LiteServerAccountState {
            val id = reader.read(TonNodeBlockIdExt)
            val shardBlk = reader.read(TonNodeBlockIdExt)
            val shardProof = reader.readBytes()
            val proof = reader.readBytes()
            val state = reader.readBytes()
            return LiteServerAccountState(id, shardBlk, shardProof, proof, state)
        }

        override fun encode(writer: TlWriter, value: LiteServerAccountState) {
            writer.write(TonNodeBlockIdExt, value.id)
            writer.write(TonNodeBlockIdExt, value.shardBlock)
            writer.writeBytes(value.shardProof)
            writer.writeBytes(value.proof)
            writer.writeBytes(value.state)
        }
    }
}
