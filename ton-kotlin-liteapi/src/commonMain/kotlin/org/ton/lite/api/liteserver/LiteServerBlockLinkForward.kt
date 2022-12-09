package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBoolTl
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBoolTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerBlockLinkForward(
    override val to_key_block: Boolean,
    override val from: TonNodeBlockIdExt,
    override val to: TonNodeBlockIdExt,
    val dest_proof: ByteArray,
    val config_proof: ByteArray,
    val signatures: LiteServerSignatureSet
) : LiteServerBlockLink {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerBlockLinkForward

        if (to_key_block != other.to_key_block) return false
        if (from != other.from) return false
        if (to != other.to) return false
        if (!dest_proof.contentEquals(other.dest_proof)) return false
        if (!config_proof.contentEquals(other.config_proof)) return false
        if (signatures != other.signatures) return false

        return true
    }

    override fun hashCode(): Int {
        var result = to_key_block.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + dest_proof.contentHashCode()
        result = 31 * result + config_proof.contentHashCode()
        result = 31 * result + signatures.hashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerBlockLinkForward(to_key_block=")
        append(to_key_block)
        append(", from=")
        append(from)
        append(", to=")
        append(to)
        append(", dest_proof=")
        append(dest_proof.encodeHex())
        append(", config_proof=")
        append(config_proof.encodeHex())
        append(", signatures=")
        append(signatures)
        append(")")
    }

    companion object : TlCodec<LiteServerBlockLinkForward> by LiteServerBlockLinkForwardTlConstructor {
        fun tlConstructor(): TlConstructor<LiteServerBlockLinkForward> = LiteServerBlockLinkForwardTlConstructor
    }
}

private object LiteServerBlockLinkForwardTlConstructor : TlConstructor<LiteServerBlockLinkForward>(
    schema = "liteServer.blockLinkForward to_key_block:Bool from:tonNode.blockIdExt to:tonNode.blockIdExt dest_proof:bytes config_proof:bytes signatures:liteServer.SignatureSet = liteServer.BlockLink"
) {
    override fun decode(input: Input): LiteServerBlockLinkForward {
        val toKeyBlock = input.readBoolTl()
        val from = input.readTl(TonNodeBlockIdExt)
        val to = input.readTl(TonNodeBlockIdExt)
        val destProof = input.readBytesTl()
        val configProof = input.readBytesTl()
        val signatures = input.readTl(LiteServerSignatureSet)
        return LiteServerBlockLinkForward(toKeyBlock, from, to, destProof, configProof, signatures)
    }

    override fun encode(output: Output, value: LiteServerBlockLinkForward) {
        output.writeBoolTl(value.to_key_block)
        output.writeTl(TonNodeBlockIdExt, value.from)
        output.writeTl(TonNodeBlockIdExt, value.to)
        output.writeBytesTl(value.dest_proof)
        output.writeBytesTl(value.config_proof)
        output.writeTl(LiteServerSignatureSet, value.signatures)
    }
}
