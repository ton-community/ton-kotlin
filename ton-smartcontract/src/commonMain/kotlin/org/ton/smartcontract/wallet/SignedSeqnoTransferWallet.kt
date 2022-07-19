package org.ton.smartcontract.wallet

import org.ton.smartcontract.wallet.builder.SignedSeqnoTransferBuilder

/**
 * Base class for almost every wallet contract out there, uses private key for authorization but also requires seqno
 */
interface SignedSeqnoTransferWallet<TMB : SignedSeqnoTransferBuilder> : SignedTransferWallet<TMB>,
    SeqnoTransferWallet<TMB>
