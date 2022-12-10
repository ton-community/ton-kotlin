package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.Block
import org.ton.boc.BagOfCells
import org.ton.crypto.hex
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl
import org.ton.tlb.loadTlb

@Serializable
data class LiteServerBlockData(
    val id: TonNodeBlockIdExt,
    val data: ByteArray
) {
    fun dataBagOfCells(): BagOfCells = BagOfCells(data)

    fun toBlock(): Block = dataBagOfCells().first().parse {
        loadTlb(Block)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerBlockData) return false
        if (id != other.id) return false
        if (!data.contentEquals(other.data)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }

    override fun toString(): String = buildString {
        append("LiteServerBlockData(id=")
        append(id)
        append(", data=")
        append(hex(data))
        append(")")
    }

    companion object : TlConstructor<LiteServerBlockData>(
        schema = "liteServer.blockData id:tonNode.blockIdExt data:bytes = liteServer.BlockData"
    ) {
        override fun decode(input: Input): LiteServerBlockData {
            val id = input.readTl(TonNodeBlockIdExt)
            val data = input.readBytesTl()
            return LiteServerBlockData(id, data)
        }

        override fun encode(output: Output, value: LiteServerBlockData) {
            output.writeTl(TonNodeBlockIdExt, value.id)
            output.writeBytesTl(value.data)
        }
    }
}
