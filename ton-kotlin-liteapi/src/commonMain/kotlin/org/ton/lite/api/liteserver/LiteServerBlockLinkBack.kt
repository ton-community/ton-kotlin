package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*
import kotlin.jvm.JvmStatic

@Serializable
@SerialName("liteServer.blockLinkBack")
public data class LiteServerBlockLinkBack(
    @SerialName("to_key_block")
    override val toKeyBlock: Boolean,
    override val from: TonNodeBlockIdExt,
    override val to: TonNodeBlockIdExt,
    @SerialName("dest_proof")
    val destProof: ByteArray,
    val proof: ByteArray,
    @SerialName("state_proof")
    val stateProof: ByteArray
) : LiteServerBlockLink {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerBlockLinkBack) return false

        if (toKeyBlock != other.toKeyBlock) return false
        if (from != other.from) return false
        if (to != other.to) return false
        if (!destProof.contentEquals(other.destProof)) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!stateProof.contentEquals(other.stateProof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = toKeyBlock.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + destProof.contentHashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + stateProof.contentHashCode()
        return result
    }

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
        val destProof = reader.readBytes()
        val proof = reader.readBytes()
        val stateProof = reader.readBytes()
        return LiteServerBlockLinkBack(toKeyBlock, from, to, destProof, proof, stateProof)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockLinkBack) {
        writer.writeBoolean(value.toKeyBlock)
        writer.write(TonNodeBlockIdExt, value.from)
        writer.write(TonNodeBlockIdExt, value.to)
        writer.writeBytes(value.destProof)
        writer.writeBytes(value.proof)
        writer.writeBytes(value.stateProof)
    }
}
