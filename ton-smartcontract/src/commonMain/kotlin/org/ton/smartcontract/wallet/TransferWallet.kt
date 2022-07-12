package org.ton.smartcontract.wallet

import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.builder.TransferBuilder

/**
 * Wallet contract we have a permission to perform operations on, such as transfers etc.
 */
interface TransferWallet<TMB : TransferBuilder> : Wallet {
    /**
     * Perform a transfer of funds
     */
    suspend fun transfer(lite_api: LiteApi, builder: TMB.() -> Unit)
}
