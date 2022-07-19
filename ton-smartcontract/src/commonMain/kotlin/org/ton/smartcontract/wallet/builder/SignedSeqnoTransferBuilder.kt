package org.ton.smartcontract.wallet.builder

import org.ton.cell.Cell
import org.ton.cell.CellBuilder

interface SignedSeqnoTransferBuilder : SignedTransferBuilder, SeqnoTransferBuilder {
    override fun createData(builder: CellBuilder.() -> Unit): Cell =
        super<SignedTransferBuilder>.createData {
            super<SeqnoTransferBuilder>.createData(builder)
        }
}
