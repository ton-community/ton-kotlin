package org.ton.smartcontract.wallet

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackValue
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId

/**
 * Most wallets implement sequence number (seqno) in order to circumvent replay attacks
 */
interface SeqnoWallet : Wallet {
    /**
     * Get most recent seqno
     */
    suspend fun seqno(liteApi: LiteApi): Int = seqno(liteApi, liteApi.getMasterchainInfo().last)

    /**
     * Get seqno value as of [referenceBlockId]
     */
    suspend fun seqno(liteApi: LiteApi, referenceBlockId: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, referenceBlockId, liteServerAccountId, "seqno")
        require(result.exitCode == 0) { "failed to run smc method `seqno` with exit code ${result.exitCode}" }
        require(result.resultValues().orEmpty().size == 1 && (result.first() !is VmStackValue.TinyInt))
        { "failed to get proper result for `seqno` smc method" }
        return (result.first() as VmStackValue.TinyInt).value.toInt()
    }
}
