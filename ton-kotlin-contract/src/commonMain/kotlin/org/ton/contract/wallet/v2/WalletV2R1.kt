package org.ton.contract.wallet.v2

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi

/**
 * Wallet v2 revision 1
 *
 * In this revision additional `valid_until` get-method was introduced:
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/fd7a8de9708c9ece8d802890519735b55bc99a8e/crypto/smartcont/new-wallet-v2.fif)
 */
class WalletV2R1(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    timeout: Long = 60
) : AbstractWalletV2(liteApi, privateKey, workchainId, timeout) {
    override val name: String = "v2R1"
    override val code: Cell = CODE

    companion object {
        val CODE: Cell =
            Cell("FF0020DD2082014C97BA9730ED44D0D70B1FE0A4F2608308D71820D31FD31F01F823BBF263ED44D0D31FD3FFD15131BAF2A103F901541042F910F2A2F800029320D74A96D307D402FB00E8D1A4C8CB1FCBFFC9ED54")
    }
}
