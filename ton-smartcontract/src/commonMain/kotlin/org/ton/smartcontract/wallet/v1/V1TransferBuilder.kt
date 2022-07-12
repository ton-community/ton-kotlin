package org.ton.smartcontract.wallet.v1

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Coins
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi
import org.ton.smartcontract.wallet.builder.BasicTransferBuilder

class V1TransferBuilder(
    private_key: PrivateKeyEd25519,
    override val lite_api: LiteApi,
    override val src: MsgAddressInt,
    override var seqno: Int = 0,
    override var payload: Cell = Cell.of(),
    override var bounce: Boolean = true,
    override var send_mode: Int = 3,
    override var dest_state_init: StateInit? = null,
) :
    BasicTransferBuilder(
        private_key,
        lite_api,
        src,
        seqno,
        payload,
        bounce,
        send_mode,
        dest_state_init
    ) {
    override lateinit var dest: MsgAddressInt
    override lateinit var amount: Coins
}
