package org.ton.smartcontract.wallet

/**
 * A wallet which requires you to supply seqno in order to
 */
interface SeqnoTransferWalletContract<TMB : SeqnoTransferMessageBuilder> : TransferWalletContract<TMB>,
    SeqnoWalletContract
