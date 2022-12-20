package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
public data class LiteServerBlockLinkForward(
    @SerialName("to_key_block")
    override val toKeyBlock: Boolean,
    override val from: TonNodeBlockIdExt,
    override val to: TonNodeBlockIdExt,
    @SerialName("dest_proof")
    val destProof: BagOfCells,
    @SerialName("config_proof")
    val configProof: BagOfCells,
    val signatures: LiteServerSignatureSet
) : LiteServerBlockLink {
    public companion object : TlCodec<LiteServerBlockLinkForward> by LiteServerBlockLinkForwardTlConstructor {
        @JvmStatic
        public fun tlConstructor(): TlConstructor<LiteServerBlockLinkForward> = LiteServerBlockLinkForwardTlConstructor
    }
}

private object LiteServerBlockLinkForwardTlConstructor : TlConstructor<LiteServerBlockLinkForward>(
    schema = "liteServer.blockLinkForward to_key_block:Bool from:tonNode.blockIdExt to:tonNode.blockIdExt dest_proof:bytes config_proof:bytes signatures:liteServer.SignatureSet = liteServer.BlockLink"
) {
    override fun decode(reader: TlReader): LiteServerBlockLinkForward {
        val toKeyBlock = reader.readBoolean()
        val from = reader.read(TonNodeBlockIdExt)
        val to = reader.read(TonNodeBlockIdExt)
        val destProof = reader.readBoc()
        val configProof = reader.readBoc()
        val signatures = reader.read(LiteServerSignatureSet)
        return LiteServerBlockLinkForward(toKeyBlock, from, to, destProof, configProof, signatures)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockLinkForward) {
        writer.writeBoolean(value.toKeyBlock)
        writer.write(TonNodeBlockIdExt, value.from)
        writer.write(TonNodeBlockIdExt, value.to)
        writer.writeBoc(value.destProof)
        writer.writeBoc(value.configProof)
        writer.write(LiteServerSignatureSet, value.signatures)
    }
}
