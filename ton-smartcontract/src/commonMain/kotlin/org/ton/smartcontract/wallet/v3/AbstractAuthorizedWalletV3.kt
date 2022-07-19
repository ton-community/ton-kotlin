package org.ton.smartcontract.wallet.v3

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.smartcontract.wallet.AuthorizedEd25519SeqnoWalletContract

abstract class AbstractAuthorizedWalletV3(
    private_key: PrivateKeyEd25519,
    workchain_id: Int = 0,
    val subwallet_id: Int = DEFAULT_WALLET_ID + workchain_id,
    private val timeout: Long = 60
) : AuthorizedEd25519SeqnoWalletContract(private_key, workchain_id) {
    override fun createDataInit(): Cell = CellBuilder.createCell {
        storeUInt(0, 32) // seqno
        storeUInt(subwallet_id, 32)
        storeBytes(publicKey().key)
    }

    override fun createMessageData(seqno: Int, builder: CellBuilder.() -> Unit): Cell =
        createSignedMessageData {
            storeUInt(subwallet_id, 32)
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

