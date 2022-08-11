package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBoolTl
import org.ton.tl.constructors.readVectorTl
import org.ton.tl.constructors.writeBoolTl
import org.ton.tl.constructors.writeVectorTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerPartialBlockProof(
    val complete: Boolean,
    val from: TonNodeBlockIdExt,
    val to: TonNodeBlockIdExt,
    val steps: List<LiteServerBlockLink>
) {
    companion object : TlCodec<LiteServerPartialBlockProof> by LiteServerPartialBlockProofTlConstructor
}

private object LiteServerPartialBlockProofTlConstructor : TlConstructor<LiteServerPartialBlockProof>(
    type = LiteServerPartialBlockProof::class,
    schema = "liteServer.partialBlockProof complete:Bool from:tonNode.blockIdExt to:tonNode.blockIdExt steps:(vector liteServer.BlockLink) = liteServer.PartialBlockProof"
) {
    override fun decode(input: Input): LiteServerPartialBlockProof {
        val complete = input.readBoolTl()
        val from = input.readTl(TonNodeBlockIdExt)
        val to = input.readTl(TonNodeBlockIdExt)
        val steps = input.readVectorTl(LiteServerBlockLink)
        return LiteServerPartialBlockProof(complete, from, to, steps)
    }

    override fun encode(output: Output, value: LiteServerPartialBlockProof) {
        output.writeBoolTl(value.complete)
        output.writeTl(TonNodeBlockIdExt, value.from)
        output.writeTl(TonNodeBlockIdExt, value.to)
        output.writeVectorTl(value.steps, LiteServerBlockLink)
    }
}
