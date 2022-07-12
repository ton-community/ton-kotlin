package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.AbstractEd25519SeqnoTransferWalletContract

abstract class AbstractWalletV1(
    private val private_key: PrivateKeyEd25519,
    override val workchain_id: Int = 0,
) : AbstractEd25519SeqnoTransferWalletContract<WalletV1TransferMessageBuilder>(private_key, workchain_id) {
    override suspend fun transfer(liteApi: LiteApi, builder: WalletV1TransferMessageBuilder.() -> Unit) {
        WalletV1TransferMessageBuilder(
            private_key = private_key,
            liteApi = liteApi,
            source = address(),
            seqno = seqno(liteApi),
        ).apply(builder)
    }
}
