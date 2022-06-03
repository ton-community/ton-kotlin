package org.ton.smartcontract.wallet

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackValue
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.smartcontract.SmartContract

interface SeqnoWallet : SmartContract {
    suspend fun seqno(): Int = seqno(liteApi.getMasterchainInfo().last)

    suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "seqno")
        return (result.first() as VmStackValue.TinyInt).value.toInt()
    }
}
