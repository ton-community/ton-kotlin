package org.ton.lite.client

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.future.asCompletableFuture
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.MsgAddressInt
import org.ton.lite.client.internal.FullAccountState
import org.ton.lite.client.internal.TransactionId
import org.ton.lite.client.internal.TransactionInfo
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

@OptIn(DelicateCoroutinesApi::class)
public actual interface LiteClientApi {

    public actual suspend fun getAccountState(accountAddress: MsgAddressInt): FullAccountState

    public fun getAccountStateAsync(executor: Executor, accountAddress: MsgAddressInt): CompletableFuture<FullAccountState> =
        GlobalScope.async(executor.asCoroutineDispatcher()) {
            getAccountState(accountAddress)
        }.asCompletableFuture()

    public actual suspend fun getAccountState(
        accountAddress: MsgAddressInt,
        blockId: TonNodeBlockIdExt
    ): FullAccountState

    public fun getAccountStateAsync(
        executor: Executor,
        accountAddress: MsgAddressInt,
        blockId: TonNodeBlockIdExt
    ): CompletableFuture<FullAccountState> = GlobalScope.async(executor.asCoroutineDispatcher()) {
        getAccountState(accountAddress, blockId)
    }.asCompletableFuture()

    public actual suspend fun getTransactions(
        accountAddress: MsgAddressInt,
        fromTransactionId: TransactionId,
        count: Int
    ): List<TransactionInfo>

    public fun getTransactionsAsync(
        executor: Executor,
        accountAddress: MsgAddressInt,
        fromTransactionId: TransactionId,
        count: Int
    ): CompletableFuture<List<TransactionInfo>> = GlobalScope.async(executor.asCoroutineDispatcher()) {
        getTransactions(accountAddress, fromTransactionId, count)
    }.asCompletableFuture()
}
