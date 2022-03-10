package ton.cell

import ton.bitstring.BitStringBuilder
import ton.bitstring.BitStringWriter

class CellBuilder(
    private val data: BitStringBuilder = BitStringBuilder(),
    override var cellReferences: MutableList<Cell> = ArrayList(),
) : CellWriter, BitStringWriter by data {
    override var writePosition: Int
        get() = data.writePosition
        set(value) {
            data.writePosition = value
        }

    fun toCell() = Cell(data.toBitString(), cellReferences)
}

fun buildCell(builder: CellBuilder.() -> Unit): Cell = CellBuilder().apply(builder).toCell()