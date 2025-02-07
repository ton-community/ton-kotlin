package org.ton.contract

import org.ton.kotlin.account.Account
import org.ton.kotlin.block.BlockId
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellSlice
import org.ton.kotlin.message.Message
import org.ton.kotlin.message.address.IntAddr
import org.ton.kotlin.message.info.ExtInMsgInfo

public interface Provider {
    public suspend fun getLastBlock(): BlockId

    public suspend fun getAccountState(address: IntAddr, blockId: BlockId? = null): Account?

    public suspend fun sendMessage(message: Message<ExtInMsgInfo, CellSlice>, context: CellContext = CellContext.EMPTY)
}