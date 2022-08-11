package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
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

data class LiteServerBlockLinkBack(
    override val to_key_block: Boolean,
    override val from: TonNodeBlockIdExt,
    override val to: TonNodeBlockIdExt,
    val dest_proof: ByteArray,
    val proof: ByteArray,
    val state_proof: ByteArray
) : LiteServerBlockLink {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerBlockLinkBack

        if (to_key_block != other.to_key_block) return false
        if (from != other.from) return false
        if (to != other.to) return false
        if (!dest_proof.contentEquals(other.dest_proof)) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!state_proof.contentEquals(other.state_proof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = to_key_block.hashCode()
        result = 31 * result + from.hashCode()
        result = 31 * result + to.hashCode()
        result = 31 * result + dest_proof.contentHashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + state_proof.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerBlockLinkBack(to_key_block=")
        append(to_key_block)
        append(", from=")
        append(from)
        append(", to=")
        append(to)
        append(", dest_proof=")
        append(dest_proof.encodeHex())
        append(", proof=")
        append(proof.encodeHex())
        append(", state_proof=")
        append(state_proof.encodeHex())
        append(")")
    }

    companion object : TlCodec<LiteServerBlockLinkBack> by LiteServerBlockLinkBackTlConstructor {
        fun tlConstructor(): TlConstructor<LiteServerBlockLinkBack> = LiteServerBlockLinkBackTlConstructor
    }
}

private object LiteServerBlockLinkBackTlConstructor : TlConstructor<LiteServerBlockLinkBack>(
    type = LiteServerBlockLinkBack::class,
    schema = "liteServer.blockLinkBack to_key_block:Bool from:tonNode.blockIdExt to:tonNode.blockIdExt dest_proof:bytes proof:bytes state_proof:bytes = liteServer.BlockLink"
) {
    override fun decode(input: Input): LiteServerBlockLinkBack {
        val toKeyBlock = input.readBoolTl()
        val from = input.readTl(TonNodeBlockIdExt)
        val to = input.readTl(TonNodeBlockIdExt)
        val destProof = input.readBytesTl()
        val proof = input.readBytesTl()
        val stateProof = input.readBytesTl()
        return LiteServerBlockLinkBack(toKeyBlock, from, to, destProof, proof, stateProof)
    }

    override fun encode(output: Output, value: LiteServerBlockLinkBack) {
        output.writeBoolTl(value.to_key_block)
        output.writeTl(TonNodeBlockIdExt, value.from)
        output.writeTl(TonNodeBlockIdExt, value.to)
        output.writeBytesTl(value.dest_proof)
        output.writeBytesTl(value.proof)
        output.writeBytesTl(value.state_proof)
    }
}
