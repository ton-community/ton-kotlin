package org.ton.contract.wallet.v2

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.SeqnoWallet
import org.ton.smartcontract.wallet.WalletContract

abstract class AbstractWalletV2(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    private val timeout: Long = 60
) : WalletContract(liteApi, privateKey, workchainId), SeqnoWallet {

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
}
