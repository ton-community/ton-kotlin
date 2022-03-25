package ton.cell

import ton.bitstring.BitStringBuilder
import ton.bitstring.BitStringWriter

interface CellBuilder : CellWriter {
    override var writePosition: Int

    fun toCell(): Cell
}

private class CellBuilderImpl(
    private val data: BitStringBuilder = BitStringBuilder(),
    override var cellReferences: MutableList<Cell> = ArrayList(),
) : CellBuilder, BitStringWriter by data {
    override var writePosition: Int
        get() = data.writePosition
        set(value) {
            data.writePosition = value
        }

    override fun toCell() = Cell(data.toBitString(), cellReferences)
}

fun CellBuilder(): CellBuilder = CellBuilderImpl()

fun buildCell(builder: CellBuilder.() -> Unit): Cell = CellBuilder().apply(builder).toCell()