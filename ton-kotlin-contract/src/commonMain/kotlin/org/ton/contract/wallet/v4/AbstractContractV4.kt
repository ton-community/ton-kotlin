package org.ton.contract.wallet.v4

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.contract.wallet.GetPublicKeyContract
import org.ton.contract.wallet.SeqnoContract
import org.ton.contract.wallet.WalletContract
import org.ton.lite.api.LiteApi

abstract class AbstractContractV4(
    liteApi: LiteApi,
    privateKey: PrivateKeyEd25519,
    workchainId: Int = 0,
    val subwalletId: Int = DEFAULT_WALLET_ID + workchainId,
    private val timeout: Long = 60
) : WalletContract(liteApi, privateKey, workchainId), SeqnoContract, GetPublicKeyContract {

    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeUInt(subwalletId, 32)
        storeBytes(privateKey.publicKey().key)
        storeUInt(0, 1) // plugins dict empty
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
        storeUInt(0, 8) // op
        apply(builder)
    }

    companion object {
        const val DEFAULT_WALLET_ID = 698983191
    }
}
