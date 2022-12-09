package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.*
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerBlockTransactions(
    val id: TonNodeBlockIdExt,
    val req_count: Int,
    val incomplete: Boolean,
    val ids: List<LiteServerTransactionId>,
    val proof: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerBlockTransactions

        if (id != other.id) return false
        if (req_count != other.req_count) return false
        if (incomplete != other.incomplete) return false
        if (ids != other.ids) return false
        if (!proof.contentEquals(other.proof)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + req_count
        result = 31 * result + incomplete.hashCode()
        result = 31 * result + ids.hashCode()
        result = 31 * result + proof.contentHashCode()
        return result
    }

    companion object : TlCodec<LiteServerBlockTransactions> by LiteServerBlockTransactionsTlConstructor
}

private object LiteServerBlockTransactionsTlConstructor : TlConstructor<LiteServerBlockTransactions>(
    schema = "liteServer.blockTransactions id:tonNode.blockIdExt req_count:# incomplete:Bool ids:(vector liteServer.transactionId) proof:bytes = liteServer.BlockTransactions"
) {
    override fun decode(input: Input): LiteServerBlockTransactions {
        val id = input.readTl(TonNodeBlockIdExt)
        val reqCount = input.readIntTl()
        val incomplete = input.readBoolTl()
        val ids = input.readVectorTl(LiteServerTransactionId)
        val proof = input.readBytesTl()
        return LiteServerBlockTransactions(id, reqCount, incomplete, ids, proof)
    }

    override fun encode(output: Output, value: LiteServerBlockTransactions) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.req_count)
        output.writeBoolTl(value.incomplete)
        output.writeVectorTl(value.ids, LiteServerTransactionId)
    }
}
