package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerPartialBlockProof
import org.ton.tl.*
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeIntTl

interface LiteServerGetBlockProofFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerGetBlockProof) =
        query(query, LiteServerGetBlockProof, LiteServerPartialBlockProof)

    suspend fun getBlockProof(
        mode: Int,
        known_block: TonNodeBlockIdExt,
        target_block: TonNodeBlockIdExt?
    ) = query(LiteServerGetBlockProof(mode, known_block, target_block))

    suspend fun getBlockProof(
        known_block: TonNodeBlockIdExt,
        target_block: TonNodeBlockIdExt? = null
    ) = query(LiteServerGetBlockProof(known_block, target_block))
}

@Serializable
data class LiteServerGetBlockProof(
    val mode: Int,
    val known_block: TonNodeBlockIdExt,
    val target_block: TonNodeBlockIdExt?
) {
    constructor(
        known_block: TonNodeBlockIdExt,
        target_block: TonNodeBlockIdExt? = null
    ) : this(if (target_block != null) 1 else 0, known_block, target_block)

    companion object : TlCodec<LiteServerGetBlockProof> by LiteServerGetBlockProofTlConstructor
}

private object LiteServerGetBlockProofTlConstructor : TlConstructor<LiteServerGetBlockProof>(
    schema = "liteServer.getBlockProof mode:# known_block:tonNode.blockIdExt target_block:mode.0?tonNode.blockIdExt = liteServer.PartialBlockProof"
) {
    override fun decode(input: Input): LiteServerGetBlockProof {
        val mode = input.readIntTl()
        val knownBlock = input.readTl(TonNodeBlockIdExt)
        val targetBlock = input.readFlagTl(mode, 0, TonNodeBlockIdExt)
        return LiteServerGetBlockProof(mode, knownBlock, targetBlock)
    }

    override fun encode(output: Output, value: LiteServerGetBlockProof) {
        output.writeIntTl(value.mode)
        output.writeTl(TonNodeBlockIdExt, value.known_block)
        output.writeOptionalTl(value.mode, 0, TonNodeBlockIdExt, value.target_block)
    }
}
