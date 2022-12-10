@file:UseSerializers(Base64ByteArraySerializer::class)

package org.ton.lite.api.liteserver

import io.ktor.util.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.ton.api.tonnode.Shard
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.crypto.base64.Base64ByteArraySerializer
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerAccountState(
    val id: TonNodeBlockIdExt,
    val shard_blk: TonNodeBlockIdExt,
    val shard_proof: ByteArray,
    val proof: ByteArray,
    val state: ByteArray
) {
    fun stateBagOfCells(): BagOfCells = BagOfCells(state)
    fun shardProofBagOfCells(): BagOfCells = BagOfCells(shard_proof)

    fun check(blockIdExt: TonNodeBlockIdExt, liteServerAccountId: LiteServerAccountId) {
        check(id != blockIdExt && blockIdExt.seqno != 0.inv()) { "invalid block_id, expected: $blockIdExt, actual: $id" }
        check(shard_blk.isValidFull()) { "invalid shard_blk: $shard_blk" }
        Shard.check(id, shard_blk, shardProofBagOfCells().first())
        // TODO: check shard
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerAccountState) return false
        if (id != other.id) return false
        if (shard_blk != other.shard_blk) return false
        if (!shard_proof.contentEquals(other.shard_proof)) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!state.contentEquals(other.state)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + shard_blk.hashCode()
        result = 31 * result + shard_proof.contentHashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + state.hashCode()
        return result
    }

    override fun toString() = buildString {
        append("(id:")
        append(id)
        append(" shard_blk:")
        append(shard_blk)
        append(" shard_proof:")
        append(hex(shard_proof).uppercase())
        append(" proof:")
        append(hex(proof).uppercase())
        append(" state:")
        append(hex(state).uppercase())
        append(")")
    }

    companion object : TlConstructor<LiteServerAccountState>(
        schema = "liteServer.accountState id:tonNode.blockIdExt shardblk:tonNode.blockIdExt shard_proof:bytes proof:bytes state:bytes = liteServer.AccountState"
    ) {
        override fun decode(input: Input): LiteServerAccountState {
            val id = input.readTl(TonNodeBlockIdExt)
            val shardBlk = input.readTl(TonNodeBlockIdExt)
            val shardProof = input.readBytesTl()
            val proof = input.readBytesTl()
            val state = input.readBytesTl()
            return LiteServerAccountState(id, shardBlk, shardProof, proof, state)
        }

        override fun encode(output: Output, value: LiteServerAccountState) {
            output.writeTl(TonNodeBlockIdExt, value.id)
            output.writeTl(TonNodeBlockIdExt, value.shard_blk)
            output.writeBytesTl(value.shard_proof)
            output.writeBytesTl(value.proof)
            output.writeBytesTl(value.state)
        }
    }
}
