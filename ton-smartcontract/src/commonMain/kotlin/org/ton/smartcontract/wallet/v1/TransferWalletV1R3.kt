package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519

class TransferWalletV1R3(private_key: PrivateKeyEd25519, override val workchain_id: Int = 0) :
    V1TransferWallet(private_key, workchain_id), WalletV1R3
