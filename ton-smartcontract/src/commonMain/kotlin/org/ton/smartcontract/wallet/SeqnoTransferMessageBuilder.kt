package org.ton.smartcontract.wallet

import org.ton.cell.Cell
import org.ton.cell.CellBuilder

interface SeqnoTransferMessageBuilder : TransferMessageBuilder {
    var seqno: Int

    override fun buildData(builder: CellBuilder.() -> Unit): Cell = super.buildData {
        storeUInt(seqno, 32)
        apply(builder)
    }
}
