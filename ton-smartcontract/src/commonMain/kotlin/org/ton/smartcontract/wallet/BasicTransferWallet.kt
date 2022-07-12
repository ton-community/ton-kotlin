package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.smartcontract.wallet.builder.SignedSeqnoTransferBuilder

abstract class BasicTransferWallet<TMB : SignedSeqnoTransferBuilder>(
    private val private_key: PrivateKeyEd25519,
    override val workchain_id: Int = 0
) : SignedSeqnoTransferWallet<TMB> {
    override fun publicKey(): PublicKeyEd25519 = private_key.publicKey()

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeBytes(publicKey().key)
    }
}
