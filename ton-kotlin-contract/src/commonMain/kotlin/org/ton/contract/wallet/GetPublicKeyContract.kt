package org.ton.contract.wallet

import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackInt
import org.ton.contract.Contract
import org.ton.lite.api.liteserver.LiteServerAccountId

interface GetPublicKeyContract : Contract {
    suspend fun getPublicKey(): PublicKeyEd25519 = getPublicKey(liteClient.getLastBlockId())

    suspend fun getPublicKey(blockIdExt: TonNodeBlockIdExt): PublicKeyEd25519 {
        val address = address()
        val liteServerAccountId = LiteServerAccountId(address.workchainId, address.address.toByteArray())
        val result = liteClient.runSmcMethod(
            address = liteServerAccountId,
            methodName = "get_public_key",
            blockId = blockIdExt
        )
        val rawPublicKey = (result.first() as VmStackInt).value.toByteArray()
        return PublicKeyEd25519(rawPublicKey)
    }
}
