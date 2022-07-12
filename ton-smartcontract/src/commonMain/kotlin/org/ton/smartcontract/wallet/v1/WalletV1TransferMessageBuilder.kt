package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Coins
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.AbstractEd25519SeqnoTransferMessageBuilder

class WalletV1TransferMessageBuilder(
    private_key: PrivateKeyEd25519,
    override val liteApi: LiteApi,
    override val source: MsgAddressInt,
    override var seqno: Int,
    override var payload: Cell = Cell.of(),
    override var bounce: Boolean = true,
    override var sendMode: Int = 3,
    override var destinationStateInit: StateInit? = null,
) :
    AbstractEd25519SeqnoTransferMessageBuilder(
        private_key,
        liteApi,
        source,
        seqno,
        payload,
        bounce,
        sendMode,
        destinationStateInit
    ) {
    override lateinit var destination: MsgAddressInt
    override lateinit var amount: Coins
}
