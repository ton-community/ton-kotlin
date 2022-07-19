package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.BasicTransferWallet

abstract class V1TransferWallet(private val private_key: PrivateKeyEd25519, override val workchain_id: Int) :
    BasicTransferWallet<V1TransferBuilder>(private_key) {
    override suspend fun beginTransfer(lite_api: LiteApi): V1TransferBuilder =
        V1TransferBuilder(
            private_key = private_key,
            lite_api = lite_api,
            src = address(),
            seqno = seqno(lite_api)
        )
}
