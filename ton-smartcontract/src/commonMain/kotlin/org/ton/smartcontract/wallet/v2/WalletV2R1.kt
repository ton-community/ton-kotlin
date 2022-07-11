package org.ton.smartcontract.wallet.v2

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.cell.Cell

/**
 * Wallet v2 revision 1
 *
 * In this revision additional `valid_until` get-method was introduced:
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/fd7a8de9708c9ece8d802890519735b55bc99a8e/crypto/smartcont/new-wallet-v2.fif)
 */
class WalletV2R1(
    private_key: PrivateKeyEd25519,
    workchain_id: Int = 0,
    timeout: Long = 60,
) : AbstractAuthorizedWalletV2(private_key, workchain_id, timeout) {
    override fun createCodeInit(): Cell =
        Cell("FF0020DD2082014C97BA9730ED44D0D70B1FE0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED54")
}
