package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.blockTransactions")
public data class LiteServerBlockTransactions(
    val id: TonNodeBlockIdExt,
    @SerialName("req_count")
    val reqCount: Int,
    val incomplete: Boolean,
    val ids: List<LiteServerTransactionId>,
    val proof: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerBlockTransactions) return false
        if (id != other.id) return false
        if (reqCount != other.reqCount) return false
        if (incomplete != other.incomplete) return false
        if (ids != other.ids) return false
        if (!proof.contentEquals(other.proof)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + reqCount
        result = 31 * result + incomplete.hashCode()
        result = 31 * result + ids.hashCode()
        result = 31 * result + proof.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerBlockTransactions> by LiteServerBlockTransactionsTlConstructor
}

private object LiteServerBlockTransactionsTlConstructor : TlConstructor<LiteServerBlockTransactions>(
    schema = "liteServer.blockTransactions id:tonNode.blockIdExt req_count:# incomplete:Bool ids:(vector liteServer.transactionId) proof:bytes = liteServer.BlockTransactions"
) {
    override fun decode(reader: TlReader): LiteServerBlockTransactions {
        val id = reader.read(TonNodeBlockIdExt)
        val reqCount = reader.readInt()
        val incomplete = reader.readBoolean()
        val ids = List(reader.readInt()) {
            reader.read(LiteServerTransactionId)
        }
        val proof = reader.readBytes()
        return LiteServerBlockTransactions(id, reqCount, incomplete, ids, proof)
    }

    override fun encode(writer: TlWriter, value: LiteServerBlockTransactions) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeInt(value.reqCount)
        writer.writeBoolean(value.incomplete)
        writer.writeInt(value.ids.size)
        value.ids.forEach {
            writer.write(LiteServerTransactionId, it)
        }
    }
}
