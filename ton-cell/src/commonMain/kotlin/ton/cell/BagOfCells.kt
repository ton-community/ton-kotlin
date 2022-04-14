package ton.cell

import io.ktor.util.*
import io.ktor.utils.io.core.*
import ton.bitstring.BitString
import kotlin.experimental.and
import kotlin.math.ceil
import kotlin.math.floor

private const val BOC_GENERIC_MAGIC = 0xB5EE9C72.toInt()
private const val BOC_INDEXED_MAGIC = 0x68FF65F3
private const val BOC_INDEXED_CRC32C_MAGIC = 0xACC3A728.toInt()

data class BagOfCells(
    val roots: List<Cell>,
    val isIndexed: Boolean = false,
    val crc32hash: ByteArray? = null
) : Iterable<Cell> by roots {
    constructor(
        root: Cell,
        isIndexed: Boolean = false,
        crc32hash: ByteArray? = null
    ) : this(listOf(root), isIndexed, crc32hash)

    fun treeWalk(): Sequence<Cell> = sequence {
        yieldAll(roots)
        roots.forEach { root ->
            yieldAll(root.treeWalk())
        }
    }.distinct()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BagOfCells

        if (roots != other.roots) return false
        if (isIndexed != other.isIndexed) return false
        if (crc32hash != null) {
            if (other.crc32hash == null) return false
            if (!crc32hash.contentEquals(other.crc32hash)) return false
        } else if (other.crc32hash != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = roots.hashCode()
        result = 31 * result + isIndexed.hashCode()
        result = 31 * result + (crc32hash?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String =
        "BagOfCells(roots=$roots, isIndexed=$isIndexed, crc32hash=${crc32hash?.let { hex(it) }})"
}

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
            TODO()
//            hasIdx = true
//            hashCrc32 = false
//            hasCacheBits = false
//            flags = 0
//            sizeBytes = readByte().toInt()
        }
        BOC_INDEXED_CRC32C_MAGIC -> {
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

    val cellsData = Array(cellsCount) { byteArrayOf() }
    val references = Array(cellsCount) { intArrayOf() }
    val cellsType = Array(cellsCount) { CellType.ORDINARY }
    repeat(cellsCount) { cellIndex ->
        val d1 = readByte()
        val d2 = readByte().toInt() and 0XFF
        val isExotic = (d1 and 8) != 0.toByte()
        val referenceCount = d1 % 8
        val fullFilledBytes = d2 % 2 == 0
        val dataSize = (d2 shr 1) + if (fullFilledBytes) 0 else 1
        val data = readBytes(dataSize)
        cellsData[cellIndex] = data
        references[cellIndex] = IntArray(referenceCount) {
            readInt(sizeBytes)
        }
        cellsType[cellIndex] = if (!isExotic) CellType.ORDINARY else CellType[data[0].toInt()]
    }

    // Resolving references & constructing cells from leaves to roots
    val doneCells = Array<Cell?>(cellsCount) { null }
    for (cellIndex in cellsCount - 1 downTo 0) {
        val cellData = cellsData[cellIndex]
        val refs = references[cellIndex].map { referenceIndex ->
            requireNotNull(doneCells[referenceIndex])
        }
        val cell = Cell(BitString(cellData), refs, cellsType[cellIndex])
        doneCells[cellIndex] = cell
    }

    // TODO: Crc32
//    val crc32c = readShort()

    val roots = rootIndexes.map { rootIndex ->
        requireNotNull(doneCells[rootIndex])
    }

    return BagOfCells(roots)
}


@OptIn(ExperimentalUnsignedTypes::class)
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
            val d1 = cell.references.size + (if (cell.isExotic) 1 else 0) * 8 + cell.maxLevel * 32
            writeByte(d1.toByte())
            val d2 = ceil(cell.bitString.size / 8.0) + floor(cell.bitString.size / 8.0)
            writeByte(d2.toInt().toByte())
            writeFully(cell.bitString.bits)
            cell.references.forEach { reference ->
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

    writeInt(BOC_GENERIC_MAGIC)

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
        writePacket(serializedCell)
    }
    if (hasCrc32c) {
        TODO()
    }
}