package org.ton.lite.client

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AddrStd
import org.ton.lite.client.internal.FullAccountState
import org.ton.lite.client.internal.TransactionId
import org.ton.lite.client.internal.TransactionInfo

public interface LiteClientApi {
    public suspend fun getAccountState(
        accountAddress: AddrStd
    ): FullAccountState

    public suspend fun getAccountState(
        accountAddress: AddrStd,
        blockId: TonNodeBlockIdExt
    ): FullAccountState

    public suspend fun getLastTransactions(
        accountAddress: AddrStd,
        fromTransactionId: TransactionId
    ): List<TransactionInfo> = getTransactions(accountAddress, fromTransactionId, 10)

    public suspend fun getTransactions(
        accountAddress: AddrStd,
        fromTransactionId: TransactionId,
        count: Int,
    ): List<TransactionInfo>
}
