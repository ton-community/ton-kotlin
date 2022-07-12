package org.ton.smartcontract.wallet

import org.ton.cell.Cell
import org.ton.cell.CellBuilder

interface Ed25519TransferMessageBuilder : TransferMessageBuilder {
    fun signCell(data: Cell): ByteArray

    override fun buildData(builder: CellBuilder.() -> Unit): Cell {
        val data = CellBuilder.createCell { apply(builder) }
        return super.buildData {
            storeBytes(signCell(data))
            storeBits(data.bits)
            storeRefs(data.refs)
        }
    }
}
