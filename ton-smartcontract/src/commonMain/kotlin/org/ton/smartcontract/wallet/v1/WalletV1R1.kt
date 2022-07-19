package org.ton.smartcontract.wallet.v1

import org.ton.cell.Cell
import org.ton.smartcontract.wallet.SeqnoWallet
import org.ton.smartcontract.wallet.SignedWallet
import org.ton.smartcontract.wallet.Wallet

/**
 * Wallet v1 revision 1
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/c2da007f4065e2520e0d948b146e0fb12fa75751/crypto/smartcont/new-wallet.fif)
 */
interface WalletV1R1 : Wallet, SeqnoWallet, SignedWallet {
    override fun createCodeInit(): Cell =
        Cell("FF0020DDA4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54")
}

private class WalletV1R1Impl(
    override val workchain_id: Int = 0
) : WalletV1R1
