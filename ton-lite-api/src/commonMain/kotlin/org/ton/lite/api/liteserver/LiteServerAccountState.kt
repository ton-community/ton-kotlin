@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.Base64ByteArraySerializer
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.base64
import org.ton.tl.TlConstructor

@Serializable
data class LiteServerAccountState(
        val id: TonNodeBlockIdExt,
        @SerialName("shardblk")
        val shardBlk: TonNodeBlockIdExt,
        @SerialName("shard_proof")
        @Serializable(Base64ByteArraySerializer::class)
        val shardProof: ByteArray,
        @Serializable(Base64ByteArraySerializer::class)
        val proof: ByteArray,
        @Serializable(Base64ByteArraySerializer::class)
        val state: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerAccountState

        if (id != other.id) return false
        if (shardBlk != other.shardBlk) return false
        if (!shardProof.contentEquals(other.shardProof)) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!state.contentEquals(other.state)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shardBlk.hashCode()
        result = 31 * result + shardProof.contentHashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + state.hashCode()
        return result
    }

    override fun toString() = buildString {
        append("LiteServerAccountState(id=")
        append(id)
        append(", shardblk=")
        append(shardBlk)
        append(", shardProof=")
        append(base64(shardProof))
        append(", proof=")
        append(base64(proof))
        append(", state=")
        append(base64(state))
        append(")")
    }

    companion object : TlConstructor<LiteServerAccountState>(
            type = LiteServerAccountState::class,
            schema = "liteServer.accountState id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes proof:bytes state:bytes = liteServer.AccountState"
    ) {
        override fun decode(input: Input): LiteServerAccountState {
            val id = input.readTl(TonNodeBlockIdExt)
            val shardBlk = input.readTl(TonNodeBlockIdExt)
            val shardProof = input.readByteArray()
            val proof = input.readByteArray()
            val state = input.readByteArray()
            return LiteServerAccountState(id, shardBlk, shardProof, proof, state)
        }

        override fun encode(output: Output, message: LiteServerAccountState) {
            output.writeTl(message.id, TonNodeBlockIdExt)
            output.writeTl(message.shardBlk, TonNodeBlockIdExt)
            output.writeByteArray(message.shardProof)
            output.writeByteArray(message.proof)
            output.writeByteArray(message.state)
        }
    }
}