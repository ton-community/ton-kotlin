package org.ton.contract.wallet

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackTinyInt
import org.ton.contract.Contract
import org.ton.lite.api.liteserver.LiteServerAccountId

interface SeqnoContract : Contract {
    suspend fun seqno(): Int = seqno(liteApi.getMasterchainInfo().last)

    suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "seqno")
        return (result.first() as VmStackTinyInt).value.toInt()
    }
}
