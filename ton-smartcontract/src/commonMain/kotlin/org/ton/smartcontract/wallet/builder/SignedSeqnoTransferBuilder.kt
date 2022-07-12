package org.ton.smartcontract.wallet.builder

import org.ton.cell.Cell
import org.ton.cell.CellBuilder

interface SignedSeqnoTransferBuilder : SignedTransferBuilder, SeqnoTransferBuilder {
    override fun buildData(builder: CellBuilder.() -> Unit): Cell =
        super<SignedTransferBuilder>.buildData {
            super<SeqnoTransferBuilder>.buildData(builder)
        }
}
