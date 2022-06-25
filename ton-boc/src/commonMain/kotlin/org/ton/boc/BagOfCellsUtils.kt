package org.ton.boc

import io.ktor.utils.io.core.*
import org.ton.bitstring.BitString
import org.ton.cell.Cell
import org.ton.cell.CellType
import org.ton.crypto.crc32c
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
            val firstByte = readByte().toInt() and 0xFF
            hasIdx = (firstByte and 128) != 0
            hashCrc32 = (firstByte and 64) != 0
            hasCacheBits = (firstByte and 32) != 0
            flags = (firstByte and 16) * 2 + (firstByte and 8)
            sizeBytes = firstByte and 0b0000_0111
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
    val cellCount = readInt(sizeBytes)
    val rootsCount = readInt(sizeBytes)
    val absentCount = readInt(sizeBytes)
    val totalCellsSize = readInt(offsetBytes)

    // Roots
    val rootIndexes = IntArray(rootsCount) {
        readInt(sizeBytes)
    }

    // Index
    val indexes = if (hasIdx) {
        IntArray(cellCount) {
            readInt(offsetBytes)
        }
    } else null

    val cellsBits = Array(cellCount) { BitString() }
    val cellsRefs = Array(cellCount) { intArrayOf() }
    val cellsType = Array(cellCount) { CellType.ORDINARY }

    repeat(cellCount) { cellIndex ->
        val d1 = readByte().toInt() and 0xFF
        val level = d1 shr 5
        val hasHashes = (d1 and 0b0001_0000) != 0
        val isExotic = (d1 and 0b0000_1000) != 0
        val refsCount = d1 and 0b0000_0111
        val isAbsent = refsCount == 0b0000_0111 && hasHashes

        // For absent cells (i.e., external references), only d1 is present, always equal to 23 + 32l.
        if (isAbsent) {
            TODO()
        } else {
            require(refsCount in 0..4) {
                "refsCount expected: 0..4 actual: $refsCount"
            }

            val d2 = readByte().toInt() and 0xFF
            val fullFilledBytes = d2 and 1 == 0
            val dataSize = (d2 shr 1) + if (fullFilledBytes) 0 else 1

            if (hasHashes) {
                val hashes = Array(level + 1) {
                    readBytes(32)
                }
                val depth = IntArray(level + 1) {
                    readShort().toInt()
                }
            }

            var cellData = readBytes(dataSize)
            if (fullFilledBytes) {
                cellData = BitString.appendAugmentTag(cellData, dataSize * 8)
            }
            val cellSize = BitString.findAugmentTag(cellData)
            cellsBits[cellIndex] = BitString(cellData, cellSize)
            cellsRefs[cellIndex] = IntArray(refsCount) { k ->
                val refIndex = readInt(sizeBytes)
                check(refIndex > cellIndex) { "bag-of-cells error: reference #$k of cell #$cellIndex is to cell #$refIndex with smaller index" }
                check(refIndex < cellCount) { "bag-of-cells error: reference #$k of cell #$cellIndex is to non-existent cell #$refIndex, only $cellCount cells are defined" }
                refIndex
            }
            cellsType[cellIndex] = if (!isExotic) CellType.ORDINARY else CellType[cellData[0].toInt()]
        }
    }

    // Resolving references & constructing cells from leaves to roots
    val cells = Array<Cell?>(cellCount) { null }
    repeat(cellCount) { cellIndex ->
        createCell(cellIndex, cells, cellsBits, cellsRefs)
    }
    // TODO: Crc32c check (calculate size of resulting bytearray)
    if (hashCrc32) {
        readIntLittleEndian()
    }

    val roots = rootIndexes.map { rootIndex ->
        requireNotNull(cells[rootIndex])
    }

    return BagOfCells(roots)
}

private fun createCell(index: Int, cells: Array<Cell?>, bits: Array<BitString>, refs: Array<IntArray>): Cell {
    var cell = cells[index]
    if (cell != null) {
        return cell
    }
    val cellBits = bits[index]
    val cellRefIndexes = refs[index]
    val cellRefs = cellRefIndexes.map { refIndex ->
        createCell(refIndex, cells, bits, refs)
    }
    cell = Cell(cellBits, cellRefs)
    cells[index] = cell
    return cell
}

fun Output.writeBagOfCells(
    bagOfCells: BagOfCells,
    hasIndex: Boolean = false,
    hasCrc32c: Boolean = false,
    hasCacheBits: Boolean = false,
    flags: Int = 0
) {
    val serializedBagOfCells = serializeBagOfCells(bagOfCells, hasIndex, hasCrc32c, hasCacheBits, flags)
    if (hasCrc32c) {
        val crc32c = crc32c(serializedBagOfCells).toInt()
        writeFully(serializedBagOfCells)
        writeIntLittleEndian(crc32c)
    } else {
        writeFully(serializedBagOfCells)
    }
}

private fun serializeBagOfCells(
    bagOfCells: BagOfCells,
    hasIndex: Boolean,
    hasCrc32c: Boolean,
    hasCacheBits: Boolean,
    flags: Int
): ByteArray = buildPacket {
    val cells = bagOfCells.toList()
    val cellsCount = cells.size
    val rootsCount = bagOfCells.roots.size
    var sizeBytes = 0
    while (cellsCount >= (1L shl (sizeBytes shl 3))) {
        sizeBytes++
    }

    val serializedCells = cells.mapIndexed { index: Int, cell: Cell ->
        buildPacket {
            val d1 = cell.refs.size + (if (cell.isExotic) 1 else 0) * 8 + cell.levelMask.level * 32
            writeByte(d1.toByte())
            val d2 = ceil(cell.bits.size / 8.0).toInt() + floor(cell.bits.size / 8.0).toInt()
            writeByte(d2.toByte())
            val cellData = if (cell.bits.size % 8 != 0) {
                BitString.appendAugmentTag(cell.bits.toByteArray(), cell.bits.size)
            } else cell.bits.toByteArray()
            writeFully(cellData)
            cell.refs.forEach { reference ->
                val refIndex = cells.indexOf(reference)
                writeInt(refIndex, sizeBytes)
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
        flagsByte = flagsByte or (1 shl 7) // has_idx:(## 1)
    }
    if (hasCrc32c) {
        flagsByte = flagsByte or (1 shl 6) // has_crc32c:(## 1)
    }
    if (hasCacheBits) {
        flagsByte = flagsByte or (1 shl 5) // has_cache_bits:(## 1)
    }
    flagsByte = flagsByte or flags // flags:(## 2) { flags = 0 }
    flagsByte = flagsByte or sizeBytes // size:(## 3) { size <= 4 }
    writeByte(flagsByte.toByte())

    writeByte(offsetBytes.toByte()) // off_bytes:(## 8) { off_bytes <= 8 }
    writeInt(cellsCount, sizeBytes) // cells:(##(size * 8))
    writeInt(rootsCount, sizeBytes) // roots:(##(size * 8)) { roots >= 1 }
    writeInt(0, sizeBytes) // absent:(##(size * 8)) { roots + absent <= cells }
    writeInt(fullSize, offsetBytes) // tot_cells_size:(##(off_bytes * 8))
    bagOfCells.roots.forEach { root ->
        val rootIndex = cells.indexOf(root)
        writeInt(rootIndex, sizeBytes)
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
}.readBytes()

private fun Input.readInt(bytes: Int): Int {
    return when (bytes) {
        1 -> readByte().toInt()
        2 -> readShort().toInt()
        3 -> (readByte().toInt() shl Short.SIZE_BITS) + readShort().toInt()
        else -> readInt()
    }
}

private fun Output.writeInt(value: Int, bytes: Int) {
    when (bytes) {
        1 -> writeByte(value.toByte())
        2 -> writeShort(value.toShort())
        3 -> {
            writeByte((value shr Short.SIZE_BITS).toByte())
            writeShort(value.toShort())
        }
        else -> writeInt(value)
    }
}
