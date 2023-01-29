package org.ton.contract.wallet

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackTinyInt
import org.ton.contract.Contract
import org.ton.lite.api.liteserver.LiteServerAccountId

interface SeqnoContract : Contract {
    public suspend fun seqno(): Int = seqno(liteClient.getLastBlockId())

    public suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val address = address()
        val liteServerAccountId = LiteServerAccountId(address.workchainId, address.address)
        val result = liteClient.runSmcMethod(
            address = liteServerAccountId,
            methodName = "get_seqno",
            blockId = blockIdExt
        )
        return (result.first() as VmStackTinyInt).value.toInt()
    }
}
