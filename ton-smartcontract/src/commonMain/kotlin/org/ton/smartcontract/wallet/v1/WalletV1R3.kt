package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.VmStackValue
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId

/**
 * Wallet v1 revision 3
 *
 * In this revision additional `get_public_key` get-method was introduced:
 *
 * [Fift-ASM source-code](https://github.com/ton-blockchain/ton/blob/master/crypto/smartcont/new-wallet.fif)
 */
class WalletV1R3(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0
) : AbstractWalletV1(liteApi, privateKey, workchainId) {
    override val name: String = "v1r3"
    override val code: Cell = CODE

    suspend fun seqno(): Int = seqno(liteApi.getMasterchainInfo().last)

    suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "seqno")
        return (result.first() as VmStackValue.TinyInt).value.toInt()
    }

    suspend fun getPublicKey(): PublicKeyEd25519 = getPublicKey(liteApi.getMasterchainInfo().last)

    suspend fun getPublicKey(blockIdExt: TonNodeBlockIdExt): PublicKeyEd25519 {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "get_public_key")
        val rawPublicKey = (result.first() as VmStackValue.Int).value.toByteArray()
        return PublicKeyEd25519(rawPublicKey)
    }

    companion object {
        val CODE: Cell =
            Cell("FF0020DD2082014C97BA218201339CBAB19C71B0ED44D0D31FD70BFFE304E0A4F260810200D71820D70B1FED44D0D31FD3FFD15112BAF2A122F901541044F910F2A2F80001D31F3120D74A96D307D402FB00DED1A4C8CB1FCBFFC9ED54")
    }
}
