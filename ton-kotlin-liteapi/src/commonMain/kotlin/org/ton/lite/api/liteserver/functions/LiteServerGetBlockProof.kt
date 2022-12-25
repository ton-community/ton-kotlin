package org.ton.lite.api.liteserver.functions

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerPartialBlockProof
import org.ton.tl.*

@Serializable
public data class LiteServerGetBlockProof(
    val mode: Int,
    val knownBlock: TonNodeBlockIdExt,
    val targetBlock: TonNodeBlockIdExt?
) : TLFunction<LiteServerGetBlockProof, LiteServerPartialBlockProof> {
    override fun tlCodec(): TlCodec<LiteServerGetBlockProof> = Companion
    override fun resultTlCodec(): TlCodec<LiteServerPartialBlockProof> = LiteServerPartialBlockProof

    public constructor(
        knownBlock: TonNodeBlockIdExt,
        targetBlock: TonNodeBlockIdExt? = null
    ) : this(if (targetBlock != null) 1 else 0, knownBlock, targetBlock)

    public companion object : TlCodec<LiteServerGetBlockProof> by LiteServerGetBlockProofTlConstructor
}

private object LiteServerGetBlockProofTlConstructor : TlConstructor<LiteServerGetBlockProof>(
    schema = "liteServer.getBlockProof mode:# known_block:tonNode.blockIdExt target_block:mode.0?tonNode.blockIdExt = liteServer.PartialBlockProof"
) {
    override fun decode(reader: TlReader): LiteServerGetBlockProof {
        val mode = reader.readInt()
        val knownBlock = reader.read(TonNodeBlockIdExt)
        val targetBlock = reader.readNullable(mode == 1) {
            read(TonNodeBlockIdExt)
        }
        return LiteServerGetBlockProof(mode, knownBlock, targetBlock)
    }

    override fun encode(writer: TlWriter, value: LiteServerGetBlockProof) {
        writer.writeInt(value.mode)
        writer.write(TonNodeBlockIdExt, value.knownBlock)
        writer.writeNullable(value.mode == 1, value.targetBlock) {
            write(TonNodeBlockIdExt, it)
        }
    }
}
