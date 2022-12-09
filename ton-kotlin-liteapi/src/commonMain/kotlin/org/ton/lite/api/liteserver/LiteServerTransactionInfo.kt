package org.ton.lite.api.liteserver

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.tl.TlCodec
import org.ton.tl.TlConstructor
import org.ton.tl.constructors.readBytesTl
import org.ton.tl.constructors.writeBytesTl
import org.ton.tl.readTl
import org.ton.tl.writeTl

@Serializable
data class LiteServerTransactionInfo(
    val id: TonNodeBlockIdExt,
    val proof: ByteArray,
    val transaction: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiteServerTransactionInfo

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

    companion object : TlCodec<LiteServerTransactionInfo> by LiteServerTransactionInfoTlConstructor
}

private object LiteServerTransactionInfoTlConstructor : TlConstructor<LiteServerTransactionInfo>(
    schema = "liteServer.transactionInfo id:tonNode.blockIdExt proof:bytes transaction:bytes = liteServer.TransactionInfo"
) {
    override fun decode(input: Input): LiteServerTransactionInfo {
        val id = input.readTl(TonNodeBlockIdExt)
        val proof = input.readBytesTl()
        val transaction = input.readBytesTl()
        return LiteServerTransactionInfo(id, proof, transaction)
    }

    override fun encode(output: Output, value: LiteServerTransactionInfo) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeBytesTl(value.proof)
        output.writeBytesTl(value.transaction)
    }
}
