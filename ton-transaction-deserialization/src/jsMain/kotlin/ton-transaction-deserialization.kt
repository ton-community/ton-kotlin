import ton.cell.BagOfCells
import ton.cell.CellBuilder
import ton.cell.slice
import ton.crypto.hex
import ton.tlb.Transaction

fun main() {
    ::CellBuilder
    ::transactionFromHexBoc
}

@JsExport
@JsName("transactionFromHexBoc")
fun transactionFromHexBoc(hex: String) =
    BagOfCells(hex(hex)).roots.first().slice().Transaction().toJsonString()