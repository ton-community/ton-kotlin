package org.ton.contract.wallet.v3

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.cell.Cell
import org.ton.contract.wallet.GetPublicKeyContract
import org.ton.lite.client.LiteClient

/**
 * Wallet v3 revision 1
 *
 * In this revision additional `get_public_key` get-method was introduced
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/3002321eb779e9936243e3b5f00be7579fb07654/crypto/smartcont/new-wallet-v3.fif)
 */
class ContractV3R2(
    liteClient: LiteClient,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    subwalletId: Int = DEFAULT_WALLET_ID + workchainId,
    timeout: Long = 60
) : AbstractContractV3(liteClient, privateKey, workchainId, subwalletId, timeout), GetPublicKeyContract {
    override val name: String = "v3r2"
    override val code: Cell = CODE

    companion object {
        val CODE: Cell =
            Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54")
    }
}
