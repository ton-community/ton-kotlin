package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.tl.*

@Serializable
public data class LiteServerBlockTransactions(
    val id: TonNodeBlockIdExt,
    val reqCount: Int,
    val incomplete: Boolean,
    val ids: Collection<LiteServerTransactionId>,
    val proof: BagOfCells
) {
    public companion object : TlCodec<LiteServerBlockTransactions> by LiteServerBlockTransactionsTlConstructor
}

private object LiteServerBlockTransactionsTlConstructor : TlConstructor<LiteServerBlockTransactions>(
    schema = "liteServer.blockTransactions id:tonNode.blockIdExt req_count:# incomplete:Bool ids:(vector liteServer.transactionId) proof:bytes = liteServer.BlockTransactions"
) {
    override fun decode(reader: TlReader): LiteServerBlockTransactions {
        val id = reader.read(TonNodeBlockIdExt)
        val reqCount = reader.readInt()
        val incomplete = reader.readBoolean()
        val ids = reader.readCollection {
            read(LiteServerTransactionId)
        }
        val proof = reader.readBoc()
        return LiteServerBlockTransactions(id, reqCount, incomplete, ids, proof)
    }

    override fun encode(output: TlWriter, value: LiteServerBlockTransactions) {
        output.write(TonNodeBlockIdExt, value.id)
        output.writeInt(value.reqCount)
        output.writeBoolean(value.incomplete)
        output.writeCollection(value.ids) {
            write(LiteServerTransactionId, it)
        }
    }
}
