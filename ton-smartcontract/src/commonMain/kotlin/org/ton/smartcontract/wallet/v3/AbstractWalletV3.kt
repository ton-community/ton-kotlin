package org.ton.smartcontract.wallet.v3

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.BitString
import org.ton.block.VmStackValue
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.smartcontract.wallet.WalletContract

abstract class AbstractWalletV3(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    val subwalletId: Int = DEFAULT_WALLET_ID + workchainId,
    private val timeout: Long = 60
) : WalletContract(liteApi, privateKey, workchainId) {

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeUInt(subwalletId, 32)
        storeBytes(privateKey.publicKey().key)
        storeUInt(0, 1) // plugins dict empty
    }

    override fun createSigningMessage(seqno: Int, builder: CellBuilder.() -> Unit): Cell = CellBuilder.createCell {
        storeUInt(subwalletId, 32)
        if (seqno == 0) {
//            storeInt(-1, 32)
            storeBits(BitString("FFFFFFFF"))
        } else {
            val now = Clock.System.now().toEpochMilliseconds() / 1000
            storeUInt(now + timeout, 32)
        }
        storeUInt(seqno, 32)
        apply(builder)
    }

    suspend fun seqno(): Int = seqno(liteApi.getMasterchainInfo().last)

    suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "seqno")
        return (result.first() as VmStackValue.TinyInt).value.toInt()
    }

    companion object {
        const val DEFAULT_WALLET_ID = 698983191
    }
}
