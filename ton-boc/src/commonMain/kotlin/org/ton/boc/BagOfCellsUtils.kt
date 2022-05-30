package org.ton.boc

import io.ktor.utils.io.core.*
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellType
import kotlin.experimental.and
import kotlin.math.ceil
import kotlin.math.floor

fun Input.readBagOfCell(): BagOfCells {
    val prefix = readInt()
    val hasIdx: Boolean
    val hashCrc32: Boolean
    val hasCacheBits: Boolean
    val flags: Int
    val sizeBytes: Int
    when (prefix) {
        BagOfCells.BOC_GENERIC_MAGIC -> {
            val flagsByte = readByte()
            hasIdx = (flagsByte and 128.toByte()) != 0.toByte()
            hashCrc32 = (flagsByte and 64.toByte()) != 0.toByte()
            hasCacheBits = (flagsByte and 32.toByte()) != 0.toByte()
            flags = (flagsByte and 16) * 2 + (flagsByte and 8)
            sizeBytes = flagsByte % 8
        }

        BagOfCells.BOC_INDEXED_MAGIC -> {
            TODO()
//            hasIdx = true
//            hashCrc32 = false
//            hasCacheBits = false
//            flags = 0
//            sizeBytes = readByte().toInt()
        }

        BagOfCells.BOC_INDEXED_CRC32C_MAGIC -> {
            TODO()
//            hasIdx = true
//            hashCrc32 = true
//            hasCacheBits = false
//            flags = 0
//            sizeBytes = readByte().toInt()
        }

        else -> throw IllegalArgumentException("Unknown magic prefix: ${prefix.toString(16)}")
    }

    // Counters
    val offsetBytes = readByte().toInt()
    val cellsCount = readInt(sizeBytes)
    val rootsCount = readInt(sizeBytes)
    val absentCount = readInt(sizeBytes)
    val totalCellsSize = readInt(offsetBytes)

    // Roots
    val rootIndexes = IntArray(rootsCount) {
        readInt(sizeBytes)
    }

    // Index
    val indexes = if (hasIdx) {
        IntArray(cellsCount) {
            readInt(offsetBytes)
        }
    } else null

    val cellsData = Array(cellsCount) { ByteArray(128) }
    val references = Array(cellsCount) { intArrayOf() }
    val cellsType = Array(cellsCount) { CellType.ORDINARY }
    repeat(cellsCount) { cellIndex ->
        val d1 = readByte().toInt() and 0xFF
        val d2 = readByte().toInt() and 0xFF
        val isExotic = (d1 and 8) != 0
        val referenceCount = d1 % 8
        val fullFilledBytes = d2 % 2 == 0
        val dataSize = (d2 shr 1) + if (fullFilledBytes) 0 else 1
        cellsData[cellIndex] = readBytes(dataSize)
        if (fullFilledBytes) {
            cellsData[cellIndex] = BitString.appendAugmentTag(cellsData[cellIndex], dataSize * 8)
        }
        references[cellIndex] = IntArray(referenceCount) {
            readInt(sizeBytes)
        }
        cellsType[cellIndex] = if (!isExotic) CellType.ORDINARY else CellType[cellsData[cellIndex][0].toInt()]
    }

    // Resolving references & constructing cells from leaves to roots
    val doneCells = Array<Cell?>(cellsCount) { null }
    for (cellIndex in cellsCount - 1 downTo 0) {
        val cellData = cellsData[cellIndex]
        val cellSize = BitString.findAugmentTag(cellData)
        val refs = references[cellIndex].map { referenceIndex ->
            requireNotNull(doneCells[referenceIndex])
        }
        val cell = Cell.of(BitString(cellData, cellSize), refs, cellsType[cellIndex])
        doneCells[cellIndex] = cell
    }

    // TODO: Crc32
//    val crc32c = readShort()

    val roots = rootIndexes.map { rootIndex ->
        requireNotNull(doneCells[rootIndex])
    }

    return BagOfCells(roots)
}

fun Output.writeBagOfCells(
    bagOfCells: BagOfCells,
    hasIndex: Boolean = false,
    hasCrc32c: Boolean = false,
    hasCacheBits: Boolean = false,
    flags: Int = 0
) {
    val cells = bagOfCells.treeWalk().distinct().toList()
    val cellsCount = cells.size
    val rootsCount = bagOfCells.roots.size
    var sizeBytes = 0
    while (cellsCount >= (1L shl (sizeBytes shl 3))) {
        sizeBytes++
    }

    val serializedCells = cells.map { cell ->
        buildPacket {
            val d1 = cell.refs.size + (if (cell.isExotic) 1 else 0) * 8 + cell.maxLevel * 32
            writeByte(d1.toByte())
            val d2 = ceil(cell.bits.size / 8.0).toInt() + floor(cell.bits.size / 8.0).toInt()
            writeByte(d2.toByte())
            val cellData = if (cell.bits.size % 8 != 0) {
                BitString.appendAugmentTag(cell.bits.toByteArray(), cell.bits.size)
            } else cell.bits.toByteArray()
//            println("WriteData: ${hex(cellData)}")
            writeFully(cellData)
            cell.refs.forEach { reference ->
                writeInt(cells.indexOf(reference), sizeBytes)
            }
        }
    }

    var fullSize = 0
    val sizeIndex = ArrayList<Int>()
    serializedCells.forEach { serializedCell ->
        sizeIndex.add(fullSize)
        fullSize += serializedCell.remaining.toInt()
    }
    var offsetBytes = 0
    while (fullSize >= (1L shl (offsetBytes shl 3))) {
        offsetBytes++
    }

    writeInt(BagOfCells.BOC_GENERIC_MAGIC)

    var flagsByte = 0
    if (hasIndex) {
        flagsByte = flagsByte or (1 shl 7)
    }
    if (hasCrc32c) {
        flagsByte = flagsByte or (1 shl 6)
    }
    if (hasCacheBits) {
        flagsByte = flagsByte or (1 shl 5)
    }
    flagsByte = flagsByte or flags
    flagsByte = flagsByte or sizeBytes

    writeByte(flagsByte.toByte())
    writeByte(offsetBytes.toByte())
    writeInt(cellsCount, sizeBytes)
    writeInt(rootsCount, sizeBytes)
    writeInt(0, sizeBytes)

    writeInt(fullSize, offsetBytes)
    bagOfCells.roots.forEach { root ->
        writeInt(cells.indexOf(root), sizeBytes)
    }
    if (hasIndex) {
        serializedCells.forEachIndexed { index, _ ->
            writeInt(sizeIndex[index], offsetBytes)
        }
    }
    serializedCells.forEach { serializedCell ->
        val bytes = serializedCell.readBytes()
        writeFully(bytes)
    }
    if (hasCrc32c) {
        TODO()
    }
}

private fun Input.readInt(bytes: Int): Int {
    var result = 0
    var b = bytes
    while (b > 0) {
        result = (result shl 8) + readByte()
        b--
    }
    return result
}

private fun Output.writeInt(value: Int, bytes: Int) {
    var v = value
    var b = bytes
    while (b > 0) {
        writeByte((v and 0xff).toByte())
        v = v shr 8
        b--
    }
}
