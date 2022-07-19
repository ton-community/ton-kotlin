package org.ton.smartcontract.wallet

import org.ton.smartcontract.wallet.builder.SeqnoTransferBuilder

/**
 * A wallet which requires you to supply seqno in order to
 */
interface SeqnoTransferWallet<TMB : SeqnoTransferBuilder> : TransferWallet<TMB>,
    SeqnoWallet
