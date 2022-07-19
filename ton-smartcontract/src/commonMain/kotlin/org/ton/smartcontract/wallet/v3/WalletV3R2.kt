package org.ton.smartcontract.wallet.v3

import io.ktor.http.*
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.cell.Cell

/**
 * Wallet v3 revision 1
 *
 * In this revision additional `get_public_key` get-method was introduced
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/3002321eb779e9936243e3b5f00be7579fb07654/crypto/smartcont/new-wallet-v3.fif)
 */
class WalletV3R2(
    private_key: PrivateKeyEd25519,
    workchain_id: Int = 0,
    subwallet_id: Int = DEFAULT_PORT + workchain_id,
    timeout: Long = 60,
) : AbstractAuthorizedWalletV3(private_key, workchain_id, subwallet_id, timeout) {
    override fun createCodeInit(): Cell =
        Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54")
}
