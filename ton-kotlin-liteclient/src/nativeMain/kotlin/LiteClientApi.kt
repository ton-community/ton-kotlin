package org.ton.lite.client

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.MsgAddressInt
import org.ton.lite.client.internal.FullAccountState
import org.ton.lite.client.internal.TransactionId
import org.ton.lite.client.internal.TransactionInfo

public actual interface LiteClientApi {
    public actual suspend fun getAccountState(
        accountAddress: MsgAddressInt
    ): FullAccountState

    public actual suspend fun getAccountState(
        accountAddress: MsgAddressInt,
        blockId: TonNodeBlockIdExt
    ): FullAccountState

    public actual suspend fun getTransactions(
        accountAddress: MsgAddressInt,
        fromTransactionId: TransactionId,
        count: Int,
    ): List<TransactionInfo>
}
