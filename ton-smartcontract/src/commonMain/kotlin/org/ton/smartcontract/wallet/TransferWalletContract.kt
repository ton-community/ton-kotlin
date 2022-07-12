package org.ton.smartcontract.wallet

import org.ton.lite.api.LiteApi

/**
 * Wallet contract we have a permission to perform operations on, such as transfers etc.
 */
interface TransferWalletContract<TMB : TransferMessageBuilder> : WalletContract {
    /**
     * Perform a transfer of funds
     */
    suspend fun transfer(liteApi: LiteApi, builder: TMB.() -> Unit)
}
