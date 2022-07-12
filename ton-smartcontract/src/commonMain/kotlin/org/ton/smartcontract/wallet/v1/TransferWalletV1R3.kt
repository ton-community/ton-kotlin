package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.BasicTransferWallet

class TransferWalletV1R3(private val private_key: PrivateKeyEd25519) :
    BasicTransferWallet<V1TransferBuilder>(private_key),
    WalletV1R3 {
    override suspend fun transfer(lite_api: LiteApi, builder: V1TransferBuilder.() -> Unit) {
        V1TransferBuilder(
            private_key = private_key,
            lite_api = lite_api,
            src = address(),
            seqno = seqno(lite_api)
        )
    }
}
