package org.ton.lite.client

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.message.address.AddrInt
import org.ton.lite.client.internal.FullAccountState
import org.ton.lite.client.internal.TransactionId
import org.ton.lite.client.internal.TransactionInfo

public interface LiteClientApi {
    public suspend fun getAccountState(
        accountAddress: AddrInt
    ): FullAccountState

    public suspend fun getAccountState(
        accountAddress: AddrInt,
        blockId: TonNodeBlockIdExt
    ): FullAccountState

    public suspend fun getTransactions(
        accountAddress: AddrInt,
        fromTransactionId: TransactionId,
        count: Int,
    ): List<TransactionInfo>
}
