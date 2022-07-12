package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.cell.Cell

/**
 * Wallet v1 revision 3
 *
 * In this revision additional `get_public_key` get-method was introduced:
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/master/crypto/smartcont/new-wallet.fif)
 */
class WalletV1R3(
    private_key: PrivateKeyEd25519,
    workchain_id: Int = 0,
) : AbstractWalletV1(private_key, workchain_id) {
    override fun createCodeInit(): Cell =
        Cell("FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54")
}
