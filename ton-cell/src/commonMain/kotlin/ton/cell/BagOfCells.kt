package ton.cell

import io.ktor.utils.io.core.*
import ton.bitstring.BitString
import kotlin.experimental.and

private const val BOC_GENERIC_MAGIC = 0xB5EE9C72.toInt()
private const val BOC_INDEXED_MAGIC = 0x68FF65F3
private const val BOC_INDEXED_CRC32C_MAGIC = 0xACC3A728.toInt()

data class BagOfCells(
    val roots: List<Cell>
) : Iterable<Cell> by roots

fun BagOfCells(byteArray: ByteArray) = ByteReadPacket(byteArray).readBagOfCells()

fun Input.readBagOfCells(): BagOfCells {
    val magic = readInt()
    val firstByte = readByte()

    val indexIncluded: Boolean
    val hasCrc: Boolean
    val hashCacheBits: Boolean
    val flags: Int
    val refSize: Int

    if (magic == BOC_GENERIC_MAGIC) {
        indexIncluded = (firstByte and 0b1000_0000.toByte()) != 0.toByte()
        hasCrc = (firstByte and 0b0100_0000.toByte()) != 0.toByte()
        hashCacheBits = (firstByte and 0b0010_0000.toByte()) != 0.toByte()
        flags = ((firstByte and 0b0001_0000.toByte()).toInt() shr 3)
        refSize = (firstByte and 0b0000_0111.toByte()).toInt()
    } else {
        throw IllegalArgumentException("unknown boc: ${magic.toString(16)}")
    }

    require(refSize in 1..4) { "refSize has to be more than 0 and less or equal 4, actual value: $refSize" }

    var offset = 5
    val offsetSize = readByte().toInt()
    require(offsetSize in 1..8) { "offsetSize has to be less or equal 8, actual value: $offsetSize" }

    val cellsCount = readInt(refSize) // cells:(##(size * 8))
    val rootsCount = readInt(refSize) // roots:(##(size * 8))
    val absentCount = readInt(refSize) // absent:(##(size * 8)) { roots + absent <= cells }

    require(!(magic == BOC_INDEXED_MAGIC || magic == BOC_INDEXED_CRC32C_MAGIC) || rootsCount <= 1) {
        "roots count has to be less or equal 1 for TAG: ${magic.toString(16)}, value: $offsetSize"
    }
    require(rootsCount <= cellsCount) {
        "roots count has to be less or equal than cells count, roots: $rootsCount, cells: $cellsCount"
    }

    val totCellsSize = readInt(offsetSize) // tot_cells_size:(##(off_bytes * 8))

    // root_list:(roots * ##(size * 8))
    val rootIndexes = if (magic == BOC_GENERIC_MAGIC) {
        IntArray(rootsCount) { index ->
            readInt(refSize) // cells:(##(size * 8))
        }
    } else intArrayOf()

    // Index processing - extract cell's sizes to check and correct future deserialization
    val cellSized = IntArray(cellsCount)
    var prevOffset = 0
    if (indexIncluded) {
        val rawIndex = IntArray(cellsCount * offsetSize)
        readFully(rawIndex)
        //TODO
    }

    // Deserialize cells
    val rawCells = arrayOfNulls<RawCell>(cellsCount)
    repeat(cellsCount) { cellIndex ->
        val cellSizeOpt = if (indexIncluded) cellSized[cellIndex] else null
        rawCells[cellIndex] = deserializeCell(this, refSize, cellIndex, cellsCount, cellSizeOpt)
    }

    // Resolving references & constructing cells from leaves to roots
    val doneCells = arrayOfNulls<Cell>(cellsCount)
    for (cellIndex in cellsCount - 1 downTo 0) {
        val rawCell = requireNotNull(rawCells[cellIndex])
        val refs = ArrayList<Cell>()
        rawCell.refs.forEach { refCellIndex ->
            val child = requireNotNull(doneCells[refCellIndex]) { "unresolved reference" }
            refs.add(child)
        }
        val cell = Cell(BitString(rawCell.data), refs, rawCell.isExotic)
        doneCells[cellIndex] = cell
    }

    val roots = ArrayList<Cell>(rootsCount)
    if (magic == BOC_GENERIC_MAGIC) {
        rootIndexes.forEach { index ->
            roots.add(requireNotNull(doneCells[index]))
        }
    } else {
        roots.add(requireNotNull(doneCells[0]))
    }

    if (hasCrc) {
        // TODO: check crc
    }

    return BagOfCells(roots)
}

private fun deserializeCell(
    src: Input,
    refSize: Int,
    cellIndex: Int,
    cellCount: Int,
    cellSizeOpt: Int? = null
): RawCell {
    val d1 = src.readByte().toInt()
    val d2 = src.readByte().toInt() and 0xFF

    val level = d1 shr 5
    val isExotic = (d1 and 8) == 8
    val refs = d1 % 8

    require(refSize <= 4) { "refs count has to be less or equal 4, actual value: $refs" }

    val fullFilledBytes = d2 % 2 == 0
    val dataSize = (d2 shr 1) + if (fullFilledBytes) 0 else 1

    val cellData = src.readBytes(dataSize)
    val references = IntArray(refs) {
        src.readInt(refSize)
    }

    return RawCell(level, cellData, references, isExotic)
}

private class RawCell(
    val level: Int,
    val data: ByteArray,
    val refs: IntArray,
    val isExotic: Boolean,
    val hashes: List<ByteArray> = emptyList(),
    val depths: List<Int> = emptyList()
)

private fun Input.readInt(bytes: Int): Int {
    var res = 0
    var b = bytes
    while (b > 0) {
        res = (res shl 8) + readByte()
        b--
    }
    return res
}