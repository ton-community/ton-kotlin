package org.ton.smartcontract.wallet

import org.ton.api.pub.PublicKeyEd25519

/**
 * Most often operations on wallets are authorized using a single Ed25519 private key
 */
interface Ed25519TransferWalletContract<TMB : Ed25519TransferMessageBuilder> : TransferWalletContract<TMB> {
    /**
     * Get wallet's public key
     */
    fun publicKey(): PublicKeyEd25519
}
