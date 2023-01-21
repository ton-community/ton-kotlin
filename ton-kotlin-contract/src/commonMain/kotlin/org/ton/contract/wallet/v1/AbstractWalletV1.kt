package org.ton.contract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.contract.wallet.WalletContract
import org.ton.lite.client.LiteClient

abstract class AbstractWalletV1(
    liteClient: LiteClient,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0
) : WalletContract(liteClient, privateKey, workchainId)
