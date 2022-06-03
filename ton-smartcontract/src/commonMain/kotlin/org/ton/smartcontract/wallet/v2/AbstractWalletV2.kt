package org.ton.smartcontract.wallet.v2

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

abstract class AbstractWalletV2(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    private val timeout: Long = 60
) : WalletContract(liteApi, privateKey, workchainId) {

    override fun createSigningMessage(seqno: Int, builder: CellBuilder.() -> Unit): Cell = CellBuilder.createCell {
        storeUInt(seqno, 32)
        if (seqno == 0) {
            storeBits(BitString("FFFFFFFF"))
        } else {
            val now = Clock.System.now().toEpochMilliseconds() / 1000
            storeUInt(now + timeout, 32)
        }
        apply(builder)
    }

    suspend fun seqno(): Int = seqno(liteApi.getMasterchainInfo().last)

    suspend fun seqno(blockIdExt: TonNodeBlockIdExt): Int {
        val liteServerAccountId = LiteServerAccountId(address())
        val result = liteApi.runSmcMethod(4, blockIdExt, liteServerAccountId, "seqno")
        return (result.first() as VmStackValue.TinyInt).value.toInt()
    }
}
