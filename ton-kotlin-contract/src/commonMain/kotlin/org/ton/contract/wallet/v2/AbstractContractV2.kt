package org.ton.contract.wallet.v2

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.contract.wallet.SeqnoContract
import org.ton.contract.wallet.WalletContract
import org.ton.lite.client.LiteClient

abstract class AbstractContractV2(
    liteClient: LiteClient,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    private val timeout: Long = 60
) : WalletContract(liteClient, privateKey, workchainId), SeqnoContract {

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
