package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.crypto.encodeHex
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerAllShardsInfo(
    val id: TonNodeBlockIdExt,
    val proof: ByteArray,
    val data: ByteArray
) {
    fun dataBagOfCells() = BagOfCells(data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerAllShardsInfo

        if (id != other.id) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerAllShardsInfo(id=")
        append(id)
        append(", proof=")
        append(proof.encodeHex())
        append(", data=")
        append(data.encodeHex())
        append(")")
    }

    companion object : TlCodec<LiteServerAllShardsInfo> by LiteServerAllShardsInfoTlConstructor
}

private object LiteServerAllShardsInfoTlConstructor : TlConstructor<LiteServerAllShardsInfo>(
    type = LiteServerAllShardsInfo::class,
    schema = "liteServer.allShardsInfo id:tonNode.blockIdExt proof:bytes data:bytes = liteServer.AllShardsInfo"
) {
    override fun decode(input: Input): LiteServerAllShardsInfo {
        val id = input.readTl(TonNodeBlockIdExt)
        val proof = input.readBytesTl()
        val data = input.readBytesTl()
        return LiteServerAllShardsInfo(id, proof, data)
    }

    override fun encode(output: Output, value: LiteServerAllShardsInfo) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeBytesTl(value.proof)
        output.writeBytesTl(value.data)
    }
}