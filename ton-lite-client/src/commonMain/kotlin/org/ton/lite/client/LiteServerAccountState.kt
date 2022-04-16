@file:UseSerializers(HexByteArraySerializer::class)

package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.adnl.TLCodec
import org.ton.crypto.HexByteArraySerializer
import org.ton.crypto.hex

@Serializable
data class LiteServerAccountState(
    val id: TonNodeBlockIdExt,
    val shardblk: TonNodeBlockIdExt,
    val shardProof: ByteArray,
    val proof: ByteArray,
    val state: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerAccountState

        if (id != other.id) return false
        if (shardblk != other.shardblk) return false
        if (!shardProof.contentEquals(other.shardProof)) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!state.contentEquals(other.state)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shardblk.hashCode()
        result = 31 * result + shardProof.contentHashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + state.hashCode()
        return result
    }

    override fun toString() =
        "LiteServerAccountState(id=$id, shardblk=$shardblk, shardProof=${hex(shardProof)}, proof=${hex(proof)}, state=${
            hex(
                state
            )
        })"

    companion object : TLCodec<LiteServerAccountState> {
        override val id: Int = 1887029073

        override fun decode(input: Input): LiteServerAccountState {
            val id = input.readTl(TonNodeBlockIdExt)
            val shardblk = input.readTl(TonNodeBlockIdExt)
            val shardProof = input.readByteArray()
            val proof = input.readByteArray()
            val state = input.readByteArray()
            return LiteServerAccountState(id, shardblk, shardProof, proof, state)
        }

        override fun encode(output: Output, message: LiteServerAccountState) {
            output.writeTl(message.id, TonNodeBlockIdExt)
            output.writeTl(message.shardblk, TonNodeBlockIdExt)
            output.writeByteArray(message.shardProof)
            output.writeByteArray(message.proof)
            output.writeByteArray(message.state)
        }
    }
}