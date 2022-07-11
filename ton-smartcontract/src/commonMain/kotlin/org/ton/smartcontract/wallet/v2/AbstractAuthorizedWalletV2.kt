package org.ton.smartcontract.wallet.v2

import kotlinx.datetime.Clock
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.smartcontract.wallet.AuthorizedEd25519SeqnoWalletContract

abstract class AbstractAuthorizedWalletV2(
    private_key: PrivateKeyEd25519,
    workchain_id: Int = 0,
    private val timeout: Long = 60
) : AuthorizedEd25519SeqnoWalletContract(private_key, workchain_id) {
    override fun createMessageData(seqno: Int, builder: CellBuilder.() -> Unit): Cell =
        super.createMessageData(seqno) {
            if (seqno == 0) {
                storeBits(BitString("FFFFFFFF"))
            } else {
                val now = Clock.System.now().toEpochMilliseconds() / 1000
                storeUInt(now + timeout, 32)
            }
            apply(builder)
        }
}
