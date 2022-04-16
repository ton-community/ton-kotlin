package org.ton.cell

import org.ton.bitstring.BitStringBuilder
import org.ton.bitstring.BitStringWriter
import kotlin.js.JsName

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

@JsName("createCellBuilder")
fun CellBuilder(): CellBuilder = CellBuilderImpl()

fun buildCell(builder: CellBuilder.() -> Unit): Cell = CellBuilder().apply(builder).toCell()