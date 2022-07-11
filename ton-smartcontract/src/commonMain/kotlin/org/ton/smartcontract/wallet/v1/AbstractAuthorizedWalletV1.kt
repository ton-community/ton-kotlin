package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.smartcontract.wallet.AuthorizedEd25519SeqnoWalletContract

abstract class AbstractAuthorizedWalletV1(
    private_key: PrivateKeyEd25519,
    override val workchain_id: Int = 0,
) : AuthorizedEd25519SeqnoWalletContract(private_key, workchain_id)
