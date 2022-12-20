package org.ton.contract.wallet

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackTinyInt
import org.ton.contract.Contract
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.functions.LiteServerGetMasterchainInfo
import org.ton.lite.api.liteserver.functions.LiteServerRunSmcMethod

interface SeqnoContract : Contract {
    public suspend fun seqno(): Int = seqno(liteApi(LiteServerGetMasterchainInfo).last)

    public suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi(LiteServerRunSmcMethod(4, blockIdExt, liteServerAccountId, "seqno"))
        return (result.parseAsVmStack()?.value?.first() as VmStackTinyInt).value.toInt()
    }
}
