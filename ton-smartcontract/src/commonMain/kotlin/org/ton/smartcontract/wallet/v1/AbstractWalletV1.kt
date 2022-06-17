package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.WalletContract

abstract class AbstractWalletV1(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0
) : WalletContract(liteApi, privateKey, workchainId)
