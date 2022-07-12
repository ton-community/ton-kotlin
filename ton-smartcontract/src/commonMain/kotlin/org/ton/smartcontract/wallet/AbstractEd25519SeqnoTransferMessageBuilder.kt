package org.ton.smartcontract.wallet

import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Coins
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi

abstract class AbstractEd25519SeqnoTransferMessageBuilder(
    private val private_key: PrivateKeyEd25519,
    override val liteApi: LiteApi,
    override val source: MsgAddressInt,
    override var seqno: Int,
    override var payload: Cell,
    override var bounce: Boolean,
    override var sendMode: Int,
    override var destinationStateInit: StateInit?
) : Ed25519TransferMessageBuilder, SeqnoTransferMessageBuilder {
    override lateinit var destination: MsgAddressInt
    override lateinit var amount: Coins

    override fun signCell(data: Cell): ByteArray = private_key.sign(data.hash())

    override fun buildData(builder: CellBuilder.() -> Unit): Cell =
        super<Ed25519TransferMessageBuilder>.buildData {
            super<SeqnoTransferMessageBuilder>.buildData(builder)
        }
}
