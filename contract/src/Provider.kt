package org.ton.contract

import org.ton.block.block.BlockId
import org.ton.block.message.address.AddrStd
import org.ton.block.org.ton.account.Account

public interface Provider {
    public suspend fun getLastBlock(): BlockId

    public suspend fun getAccountState(std: AddrStd): Account?
}