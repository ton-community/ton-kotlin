package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.SerialName
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import kotlin.jvm.JvmStatic

public data class LiteServerBlockLinkBack(
    override val toKeyBlock: Boolean,
    override val from: TonNodeBlockIdExt,
    override val to: TonNodeBlockIdExt,
    @SerialName("dest_proof")
    val destProof: BagOfCells,
    val proof: BagOfCells,
    @SerialName("state_proof")
    val stateProof: BagOfCells
) : LiteServerBlockLink {
    public companion object : TlCodec<LiteServerBlockLinkBack> by LiteServerBlockLinkBackTlConstructor {
        @JvmStatic
        public fun tlConstructor(): TlConstructor<LiteServerBlockLinkBack> = LiteServerBlockLinkBackTlConstructor
    }
}

private object LiteServerBlockLinkBackTlConstructor : TlConstructor<LiteServerBlockLinkBack>(
    schema = "liteServer.blockLinkBack to_key_block:Bool from:tonNode.blockIdExt to:tonNode.blockIdExt dest_proof:bytes proof:bytes state_proof:bytes = liteServer.BlockLink"
) {
    override fun decode(reader: TlReader): LiteServerBlockLinkBack {
        val toKeyBlock = reader.readBoolean()
        val from = reader.read(TonNodeBlockIdExt)
        val to = reader.read(TonNodeBlockIdExt)
        val destProof = reader.readBoc()
        val proof = reader.readBoc()
        val stateProof = reader.readBoc()
        return LiteServerBlockLinkBack(toKeyBlock, from, to, destProof, proof, stateProof)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockLinkBack) {
        writer.writeBoolean(value.toKeyBlock)
        writer.write(TonNodeBlockIdExt, value.from)
        writer.write(TonNodeBlockIdExt, value.to)
        writer.writeBoc(value.destProof)
        writer.writeBoc(value.proof)
        writer.writeBoc(value.stateProof)
    }
}
