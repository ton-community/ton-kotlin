package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.crypto.hex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.constructors.writeIntTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerBlockHeader(
    val id: TonNodeBlockIdExt,
    val mode: Int,
    val header_proof: ByteArray
) {
    fun headerProofBagOfCell() = BagOfCells(header_proof)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerBlockHeader

        if (id != other.id) return false
        if (mode != other.mode) return false
        if (!header_proof.contentEquals(other.header_proof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + mode
        result = 31 * result + header_proof.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("(id: ")
        append(id)
        append(" mode:")
        append(mode)
        append(" header_proof:")
        append(hex(header_proof).uppercase())
        append(")")
    }

    companion object : TlCodec<LiteServerBlockHeader> by LiteServerBlockHeaderTlConstructor
}

private object LiteServerBlockHeaderTlConstructor : TlConstructor<LiteServerBlockHeader>(
    "liteServer.blockHeader id:tonNode.blockIdExt mode:# header_proof:bytes = liteServer.BlockHeader"
) {
    override fun decode(input: Input): LiteServerBlockHeader {
        val id = input.readTl(TonNodeBlockIdExt)
        val mode = input.readIntTl()
        val headerProof = input.readBytesTl()
        return LiteServerBlockHeader(id, mode, headerProof)
    }

    override fun encode(output: Output, value: LiteServerBlockHeader) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.mode)
        output.writeBytesTl(value.header_proof)
    }
}
