package org.ton.cell

import io.ktor.utils.io.core.*

data class CellRepresentation(
    val data: ByteArray,
    val references: IntArray,
    val type: CellType,
    val level: Int
) {
    val isExotic get() = type.isExotic
}

fun Input.readCellRepresentation(referenceSize: Int): CellRepresentation {
    val d1 = readByte().toInt()
    val d2 = readByte().toInt()

    val referenceCount = d1 % 8
    val level = d1 shr 5
    val isExotic = (d1 and 8) == 8

    require(referenceCount <= 4) { "reference count has to be less or equal 4, actual value: $referenceCount" }

    val fullFilledBytes = d2 % 2 == 0
    val dataSize = (d2 shr 1) + if (fullFilledBytes) 0 else 1
    val data = readBytes(dataSize)

    val cellType = if (!isExotic) CellType.ORDINARY else CellType[data[0].toInt()]
    val references = IntArray(referenceCount) {
        readInt(referenceSize)
    }

    return CellRepresentation(data, references, cellType, level)
}

fun Output.writeCellRepresentation(cellRepresentation: CellRepresentation, referenceSize: Int) {

}