package ton.cell

import io.ktor.utils.io.core.*
import ton.bitstring.BitString
import kotlin.experimental.and

private const val BOC_GENERIC_MAGIC = 0xB5EE9C72.toInt()
private const val BOC_INDEXED_MAGIC = 0x68FF65F3
private const val BOC_INDEXED_CRC32C_MAGIC = 0xACC3A728.toInt()

data class BagOfCells(
    val roots: List<Cell>,
    val isIndexed: Boolean = false,
    val crc32hash: ByteArray? = null
) : Iterable<Cell> by roots

fun BagOfCells(byteArray: ByteArray) = ByteReadPacket(byteArray).readBagOfCell()

fun Input.readBagOfCell(): BagOfCells {
    val prefix = readInt()
    val hasIdx: Boolean
    val hashCrc32: Boolean
    val hasCacheBits: Boolean
    val flags: Int
    val sizeBytes: Int
    when (prefix) {
        BOC_GENERIC_MAGIC -> {
            val flagsByte = readByte()
            hasIdx = (flagsByte and 128.toByte()) != 0.toByte()
            hashCrc32 = (flagsByte and 64.toByte()) != 0.toByte()
            hasCacheBits = (flagsByte and 32.toByte()) != 0.toByte()
            flags = (flagsByte and 16) * 2 + (flagsByte and 8)
            sizeBytes = flagsByte % 8
        }
        BOC_INDEXED_MAGIC -> {
            hasIdx = true
            hashCrc32 = false
            hasCacheBits = false
            flags = 0
            sizeBytes = readByte().toInt()
        }
        BOC_INDEXED_CRC32C_MAGIC -> {
            hasIdx = true
            hashCrc32 = true
            hasCacheBits = false
            flags = 0
            sizeBytes = readByte().toInt()
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

    val cellsData = Array(cellsCount) { byteArrayOf() }
    val references = Array(cellsCount) { intArrayOf() }
    val cellsType = Array(cellsCount) { CellType.ORDINARY }
    repeat(cellsCount) { cellIndex ->
        val d1 = readByte()
        val d2 = readByte()
        val isExotic = (d1 and 8) != 0.toByte()
        val referenceCount = d1 % 8
        val fullFilledBytes = d2 % 2 == 0
        val dataSize = (d2.toInt() shr 1) + if (fullFilledBytes) 0 else 1
        val data = readBytes(dataSize)
        cellsData[cellIndex] = data
        references[cellIndex] = IntArray(referenceCount) {
            readInt(sizeBytes)
        }
        cellsType[cellIndex] = if (isExotic) CellType.ORDINARY else CellType[data[0].toInt()]
    }

    // Resolving references & constructing cells from leaves to roots
    val doneCells = ArrayList<Cell>(cellsCount)
    for (cellIndex in cellsCount - 1 downTo 0) {
        val cellData = cellsData[cellIndex]
        val refs = references[cellIndex].map { referenceIndex ->
            doneCells[referenceIndex]
        }
        val cell = Cell(BitString(cellData), refs, cellsType[cellIndex])
        doneCells[cellIndex] = cell
    }

    // TODO: Crc32

    val roots = rootIndexes.map { rootIndex ->
        doneCells[rootIndex]
    }

    return BagOfCells(roots)
}
