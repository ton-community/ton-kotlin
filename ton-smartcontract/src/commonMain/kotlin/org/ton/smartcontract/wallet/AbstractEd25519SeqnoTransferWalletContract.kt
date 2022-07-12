package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.cell.Cell
import org.ton.cell.CellBuilder

/**
 * Base class for almost every wallet contract out there, uses private key for authorization but also requires seqno
 */
abstract class AbstractEd25519SeqnoTransferWalletContract<TMB : AbstractEd25519SeqnoTransferMessageBuilder>(
    private val private_key: PrivateKeyEd25519,
    override val workchain_id: Int = 0,
) : Ed25519TransferWalletContract<TMB>, SeqnoTransferWalletContract<TMB> {
    override fun publicKey(): PublicKeyEd25519 = private_key.publicKey()

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeBytes(publicKey().key)
    }
}
