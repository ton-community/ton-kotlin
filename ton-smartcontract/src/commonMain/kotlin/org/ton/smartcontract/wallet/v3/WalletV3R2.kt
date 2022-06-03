package org.ton.smartcontract.wallet.v3

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackValue
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId

/**
 * Wallet v3 revision 1
 *
 * In this revision additional `get_public_key` get-method was introduced
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/3002321eb779e9936243e3b5f00be7579fb07654/crypto/smartcont/new-wallet-v3.fif)
 */
class WalletV3R2(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    subwalletId: Int = DEFAULT_WALLET_ID + workchainId,
    timeout: Long = 60
) : AbstractWalletV3(liteApi, privateKey, workchainId, subwalletId, timeout) {
    override val name: String = "v3r2"
    override val code: Cell = CODE

    suspend fun getPublicKey(): PublicKeyEd25519 = getPublicKey(liteApi.getMasterchainInfo().last)

    suspend fun getPublicKey(blockIdExt: TonNodeBlockIdExt): PublicKeyEd25519 {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "get_public_key")
        val rawPublicKey = (result.first() as VmStackValue.Int).value.toByteArray()
        return PublicKeyEd25519(rawPublicKey)
    }

    companion object {
        val CODE: Cell =
            Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54")
    }
}
