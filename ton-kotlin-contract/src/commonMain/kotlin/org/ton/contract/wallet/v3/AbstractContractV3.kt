package org.ton.contract.wallet.v3

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.contract.wallet.SeqnoContract
import org.ton.contract.wallet.WalletContract
import org.ton.lite.client.LiteClient

abstract class AbstractContractV3(
    liteClient: LiteClient,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    val subwalletId: Int = DEFAULT_WALLET_ID + workchainId,
    private val timeout: Long = 60
) : WalletContract(liteClient, privateKey, workchainId), SeqnoContract {

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeUInt(subwalletId, 32)
        storeBits(privateKey.publicKey().key)
    }

    override fun createSigningMessage(seqno: Int, builder: CellBuilder.() -> Unit): Cell = CellBuilder.createCell {
        storeUInt(subwalletId, 32)
        if (seqno == 0) {
            storeBits(BitString("FFFFFFFF"))
        } else {
            val now = Clock.System.now().toEpochMilliseconds() / 1000
            storeUInt(now + timeout, 32)
        }
        storeUInt(seqno, 32)
        apply(builder)
    }

    companion object {
        const val DEFAULT_WALLET_ID = 698983191
    }
}
