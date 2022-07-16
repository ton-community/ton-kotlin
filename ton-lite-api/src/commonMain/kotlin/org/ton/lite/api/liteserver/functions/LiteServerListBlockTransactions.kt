package org.ton.lite.api.liteserver.functions

import io.ktor.utils.io.core.*
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.api.liteserver.LiteServerBlockTransactions
import org.ton.lite.api.liteserver.LiteServerTransactionId3
import org.ton.tl.*
import org.ton.tl.constructors.readBoolTl
import org.ton.tl.constructors.readIntTl
import org.ton.tl.constructors.writeBoolTl
import org.ton.tl.constructors.writeIntTl

interface LiteServerListBlockTransactionsFunction : LiteServerQueryFunction {
    suspend fun query(query: LiteServerListBlockTransactions) =
        query(query, LiteServerListBlockTransactions, LiteServerBlockTransactions)

    suspend fun listBlockTransactions(
        id: TonNodeBlockIdExt,
        mode: Int,
        count: Int,
        after: LiteServerTransactionId3?
    ) = query(LiteServerListBlockTransactions(id, mode, count, after))
}

@Serializable
data class LiteServerListBlockTransactions(
    val id: TonNodeBlockIdExt,
    val mode: Int,
    val count: Int,
    val after: LiteServerTransactionId3?,
    val reverse_order: Boolean? = true,
    val want_proof: Boolean? = true
) {
    companion object : TlCodec<LiteServerListBlockTransactions> by LiteServerListBlockTransactionsTlConstructor
}

private object LiteServerListBlockTransactionsTlConstructor : TlConstructor<LiteServerListBlockTransactions>(
    type = LiteServerListBlockTransactions::class,
    schema = "liteServer.listBlockTransactions id:tonNode.blockIdExt mode:# count:# after:mode.7?liteServer.transactionId3 reverse_order:mode.6?true want_proof:mode.5?true = liteServer.BlockTransactions"
) {
    override fun decode(input: Input): LiteServerListBlockTransactions {
        val id = input.readTl(TonNodeBlockIdExt)
        val mode = input.readIntTl()
        val count = input.readIntTl()
        val after = input.readFlagTl(mode, 7, LiteServerTransactionId3)
        val reverseOrder = input.readFlagTl(mode, 6) { input.readBoolTl() }
        val wantProof = input.readFlagTl(mode, 5) { input.readBoolTl() }
        return LiteServerListBlockTransactions(id, mode, count, after, reverseOrder, wantProof)
    }

    override fun encode(output: Output, value: LiteServerListBlockTransactions) {
        output.writeTl(TonNodeBlockIdExt, value.id)
        output.writeIntTl(value.mode)
        output.writeIntTl(value.count)
        output.writeOptionalTl(value.mode, 7, LiteServerTransactionId3, value.after)
        output.writeOptionalTl(value.mode, 6) { output.writeBoolTl(true) }
        output.writeOptionalTl(value.mode, 5) { output.writeBoolTl(true) }
    }
}