package org.ton.smartcontract.wallet

import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackValue
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId

/**
 * Most wallets implement method `get_public_key`
 */
interface SignedWallet : Wallet {
    /**
     * Get most recent public_key
     */
    suspend fun getPublicKey(liteApi: LiteApi): PublicKeyEd25519 =
        getPublicKey(liteApi, liteApi.getMasterchainInfo().last)

    /**
     * Get public key value as of [referenceBlockId]
     */
    suspend fun getPublicKey(liteApi: LiteApi, referenceBlockId: TonNodeBlockIdExt): PublicKeyEd25519 {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, referenceBlockId, liteServerAccountId, "get_public_key")
        require(result.exitCode == 0) { "failed to run smc method `get_public_key` with exit code ${result.exitCode}" }
        require(result.resultValues().orEmpty().size == 1 && (result.first() !is VmStackValue.Int))
        { "failed to get proper result for `get_public_key` smc method" }
        val rawPublicKey = (result.first() as VmStackValue.Int).value.toByteArray()
        return PublicKeyEd25519(rawPublicKey)
    }
}
