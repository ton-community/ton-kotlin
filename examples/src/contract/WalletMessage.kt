package org.ton.kotlin.examples.contract

import org.ton.api.pk.PrivateKey
import org.ton.bitstring.BitString
import org.ton.contract.wallet.WalletTransfer

interface WalletMessage {
    val seqno: Int
    val transfers: List<WalletTransfer>

    fun sign(privateKey: PrivateKey): SignedWalletMessage
}

interface SignedWalletMessage : WalletMessage {
    val signature: BitString
}