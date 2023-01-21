package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.partialBlockProof")
public data class LiteServerPartialBlockProof(
    val complete: Boolean,
    val from: TonNodeBlockIdExt,
    val to: TonNodeBlockIdExt,
    val steps: List<LiteServerBlockLink>
) {
    public companion object : TlCodec<LiteServerPartialBlockProof> by LiteServerPartialBlockProofTlConstructor
}

private object LiteServerPartialBlockProofTlConstructor : TlConstructor<LiteServerPartialBlockProof>(
    schema = "liteServer.partialBlockProof complete:Bool from:tonNode.blockIdExt to:tonNode.blockIdExt steps:(vector liteServer.BlockLink) = liteServer.PartialBlockProof"
) {
    override fun decode(reader: TlReader): LiteServerPartialBlockProof {
        val complete = reader.readBoolean()
        val from = reader.read(TonNodeBlockIdExt)
        val to = reader.read(TonNodeBlockIdExt)
        val steps = reader.readVector {
            read(LiteServerBlockLink)
        }
        return LiteServerPartialBlockProof(complete, from, to, steps)
    }

    override fun encode(writer: TlWriter, value: LiteServerPartialBlockProof) {
        writer.writeBoolean(value.complete)
        writer.write(TonNodeBlockIdExt, value.from)
        writer.write(TonNodeBlockIdExt, value.to)
        writer.writeVector(value.steps) {
            write(LiteServerBlockLink, it)
        }
    }
}
