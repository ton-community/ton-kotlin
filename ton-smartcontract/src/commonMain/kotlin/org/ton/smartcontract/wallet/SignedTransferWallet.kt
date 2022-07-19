package org.ton.smartcontract.wallet

import org.ton.api.pub.PublicKeyEd25519
import org.ton.smartcontract.wallet.builder.SignedTransferBuilder

/**
 * Most often operations on wallets are authorized using a single Ed25519 private key
 */
interface SignedTransferWallet<TMB : SignedTransferBuilder> : TransferWallet<TMB> {
    /**
     * Get wallet's public key
     */
    fun publicKey(): PublicKeyEd25519
}
