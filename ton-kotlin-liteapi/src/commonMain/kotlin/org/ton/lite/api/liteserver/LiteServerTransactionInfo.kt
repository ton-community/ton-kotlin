package org.ton.lite.api.liteserver

import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.boc.BagOfCells
import org.ton.lite.api.liteserver.internal.readBoc
import org.ton.lite.api.liteserver.internal.writeBoc
import org.ton.tl.*

@Serializable
public data class LiteServerTransactionInfo(
    val id: TonNodeBlockIdExt,
    val proof: BagOfCells,
    val transaction: BagOfCells
) {
    public companion object : TlCodec<LiteServerTransactionInfo> by LiteServerTransactionInfoTlConstructor
}

private object LiteServerTransactionInfoTlConstructor : TlConstructor<LiteServerTransactionInfo>(
    schema = "liteServer.transactionInfo id:tonNode.blockIdExt proof:bytes transaction:bytes = liteServer.TransactionInfo"
) {
    override fun decode(reader: TlReader): LiteServerTransactionInfo {
        val id = reader.read(TonNodeBlockIdExt)
        val proof = reader.readBoc()
        val transaction = reader.readBoc()
        return LiteServerTransactionInfo(id, proof, transaction)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBoc(value.proof)
        writer.writeBoc(value.transaction)
    }
}
