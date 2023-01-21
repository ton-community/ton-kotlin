package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("liteServer.blockLinkForward")
public data class LiteServerBlockLinkForward(
    @SerialName("to_key_block")
    override val toKeyBlock: Boolean,
    override val from: TonNodeBlockIdExt,
    override val to: TonNodeBlockIdExt,
    @SerialName("dest_proof")
    val destProof: ByteArray,
    @SerialName("config_proof")
    val configProof: ByteArray,
    val signatures: LiteServerSignatureSet
) : LiteServerBlockLink {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerBlockLinkForward) return false

        if (toKeyBlock != other.toKeyBlock) return false
        if (from != other.from) return false
        if (to != other.to) return false
        if (!destProof.contentEquals(other.destProof)) return false
        if (!configProof.contentEquals(other.configProof)) return false
        if (signatures != other.signatures) return false

        return true
    }

    override fun hashCode(): Int {
        var result = toKeyBlock.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + destProof.contentHashCode()
        result = 31 * result + configProof.contentHashCode()
        result = 31 * result + signatures.hashCode()
        return result
    }

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
        val destProof = reader.readBytes()
        val configProof = reader.readBytes()
        val signatures = reader.read(LiteServerSignatureSet)
        return LiteServerBlockLinkForward(toKeyBlock, from, to, destProof, configProof, signatures)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockLinkForward) {
        writer.writeBoolean(value.toKeyBlock)
        writer.write(TonNodeBlockIdExt, value.from)
        writer.write(TonNodeBlockIdExt, value.to)
        writer.writeBytes(value.destProof)
        writer.writeBytes(value.configProof)
        writer.write(LiteServerSignatureSet, value.signatures)
    }
}
