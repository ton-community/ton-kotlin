package org.ton.smartcontract.wallet.builder

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Coins
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi

abstract class BasicTransferBuilder(
    private val private_key: PrivateKeyEd25519,
    override val lite_api: LiteApi,
    override val src: MsgAddressInt,
    override var seqno: Int,
    override var payload: Cell = Cell.of(),
    override var bounce: Boolean = true,
    override var send_mode: Int = 3,
    override var dest_state_init: StateInit? = null,
) : SignedSeqnoTransferBuilder {
    override lateinit var dest: MsgAddressInt
    override lateinit var amount: Coins

    override fun signCell(data: Cell): ByteArray = private_key.sign(data.hash())
}
