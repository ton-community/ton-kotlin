package org.ton.lite.api.liteserver

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.*

@Serializable
@SerialName("liteServer.transactionInfo")
public data class LiteServerTransactionInfo(
    val id: TonNodeBlockIdExt,
    val proof: ByteArray,
    val transaction: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LiteServerTransactionInfo) return false
        if (id != other.id) return false
        if (!proof.contentEquals(other.proof)) return false
        if (!transaction.contentEquals(other.transaction)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + proof.contentHashCode()
        result = 31 * result + transaction.contentHashCode()
        return result
    }

    public companion object : TlCodec<LiteServerTransactionInfo> by LiteServerTransactionInfoTlConstructor
}

private object LiteServerTransactionInfoTlConstructor : TlConstructor<LiteServerTransactionInfo>(
    schema = "liteServer.transactionInfo id:tonNode.blockIdExt proof:bytes transaction:bytes = liteServer.TransactionInfo"
) {
    override fun decode(reader: TlReader): LiteServerTransactionInfo {
        val id = reader.read(TonNodeBlockIdExt)
        val proof = reader.readBytes()
        val transaction = reader.readBytes()
        return LiteServerTransactionInfo(id, proof, transaction)
    }

    override fun encode(writer: TlWriter, value: LiteServerTransactionInfo) {
        writer.write(TonNodeBlockIdExt, value.id)
        writer.writeBytes(value.proof)
        writer.writeBytes(value.transaction)
    }
}
