package org.ton.boc

import io.ktor.utils.io.core.*
import org.ton.cell.Cell
import kotlin.math.min

class CachedBagOfCells(
    override val roots: List<Cell>
) : BagOfCells {
    private var cellCount = 0
    private var intermediateRefs = 0
    private var dataBytes = 0
    private var cellHashmap = HashMap<Cell, Int>()
    private var cellList = ArrayList<CellInfo>()
    private var rootList = ArrayList<RootInfo>()
    private var revisitIndex = 0
    private var cellListTmp = ArrayList<CellInfo>()

    init {
        importCells()
    }

    override fun iterator(): Iterator<Cell> = cellList.asSequence().map { it.cell }.iterator()

    override fun toByteArray(): ByteArray = buildPacket {
        writeBagOfCells(this@CachedBagOfCells)
    }.readBytes()

    override fun toString(): String = buildString {
        roots.forEachIndexed { index, cell ->
            val firstChild = index == 0
            val lastChild = index == roots.lastIndex
            Cell.toString(cell, this, "", firstChild, lastChild)
        }
    }


    private fun clearCells() {
        cellCount = 0
        intermediateRefs = 0
        dataBytes = 0
        cellHashmap.clear()
        cellList.clear()
        rootList.clear()
    }

    private fun importCells() {
        clearCells()
        roots.forEach { rootCell ->
            val index = importCell(rootCell, 0)
            rootList.add(RootInfo(rootCell, index))
        }
        reorderCells()
        check(cellCount != 0)
    }

    private fun importCell(cell: Cell, depth: Int): Int {
        check(depth <= MAX_DEPTH) { "error while importing a cell into a bag of cells: cell depth too large" }

        val currentIndex = cellHashmap[cell]
        if (currentIndex != null) {
            cellList[currentIndex].shouldCache = true
            return currentIndex
        }

        val loadedCell = cell.loadCell()
        var sumChildWeight = 1
        val referenceIndexes = ArrayList<Int>(loadedCell.refs.size)
        loadedCell.refs.forEach { reference ->
            val referenceIndex = importCell(reference, depth + 1)
            sumChildWeight += cellList[referenceIndex].weight
            intermediateRefs++
            referenceIndexes.add(referenceIndex)
        }
        check(cellList.size == cellCount)
        cellHashmap[loadedCell] = cellCount
        val cellInfo = CellInfo(
            loadedCell,
            referenceIndexes,
            hashesCount = loadedCell.levelMask.hashesCount,
            weight = min(0xFF, sumChildWeight)
        )
        cellList.add(cellInfo)
        dataBytes += loadedCell.serializedSize()
        return cellCount++
    }

    private fun reorderCells() {
        var intermediateHashes = 0
        for (i in cellList.lastIndex downTo 0) {
            val cellInfo = cellList[i]
            var sum = MAX_CELL_WEIGHT - 1
            var mask = 0
            var c = cellInfo.referencesIndexes.size
            cellInfo.referencesIndexes.forEachIndexed { j, referenceIndex ->
                val referenceInfo = cellList[referenceIndex]
                val limit = (MAX_CELL_WEIGHT - 1 + j) / cellInfo.referencesIndexes.size
                if (referenceInfo.weight <= limit) {
                    sum -= referenceInfo.weight
                    c--
                    mask = mask or (1 shl j)
                }
            }
            if (c != 0) {
                cellInfo.referencesIndexes.forEachIndexed { j, referenceIndex ->
                    if ((mask and (1 shl j)) == 0) {
                        val referenceInfo = cellList[referenceIndex]
                        val limit = sum++ / c
                        if (referenceInfo.weight > limit) {
                            referenceInfo.weight = limit
                        }
                    }
                }
            }
        }
        cellList.forEach { cellInfo ->
            var sum = 1
            cellInfo.referencesIndexes.forEach { referenceIndex ->
                sum += cellList[referenceIndex].weight
            }
            check(sum <= MAX_CELL_WEIGHT)
            if (sum <= cellInfo.weight) {
                cellInfo.weight = sum
            } else {
                cellInfo.weight = 0
                intermediateHashes += cellInfo.hashesCount
            }
        }
        var topHashes = 0
        rootList.forEach { rootInfo ->
            val cellInfo = cellList[rootInfo.index]
            if (cellInfo.weight != 0) {
                topHashes += cellInfo.hashesCount
            }
        }
        if (cellCount > 0) {
            revisitIndex = 0
            cellListTmp.clear()
            cellListTmp.ensureCapacity(cellCount)

            rootList.forEach { rootInfo ->
                revisit(rootInfo.index, Revisit.PREVISIT)
                revisit(rootInfo.index, Revisit.VISIT)
            }
            rootList.forEach { rootInfo ->
                revisit(rootInfo.index, Revisit.ALLOCATE)
            }
            rootList.forEach { rootInfo ->
                rootInfo.index = cellList[rootInfo.index].newIndex
            }
            check(revisitIndex == cellCount) { "revisitIndex: $revisitIndex, cellCount: $cellCount" }
            check(cellList.size == cellListTmp.size) { "cellList.size: ${cellList.size}, cellListTmp.size: ${cellListTmp.size}" }
            cellList = ArrayList(cellListTmp.asReversed())
            cellListTmp.clear()
        }
    }

    // force=0 : previsit (recursively until special cells are found; then visit them)
    // force=1 : visit (allocate and process all children)
    // force=2 : allocate (assign a new index; can be run only after visiting)
    private fun revisit(cellIndex: Int, force: Revisit): Int {
        check(cellIndex in 0 until cellCount)
        val cellInfo = cellList[cellIndex]

        if (cellInfo.newIndex >= 0) {
            return cellInfo.newIndex
        }
        if (force == Revisit.PREVISIT) {
            // previsit
            return if (cellInfo.newIndex != -1) {
                // already previsited or visited
                cellInfo.newIndex
            } else {
                for (j in cellInfo.referencesIndexes.lastIndex downTo 0) {
                    val childIndex = cellInfo.referencesIndexes[j]
                    val child = cellList[childIndex]
                    // either previsit or visit child, depending on whether it is special
                    val childForce = if (child.isSpecial) Revisit.VISIT else Revisit.PREVISIT
                    revisit(childIndex, childForce)
                }
                cellInfo.newIndex = -2
                cellInfo.newIndex
            }
        }
        if (force == Revisit.ALLOCATE) {
            // time to allocate
            val newIndex = revisitIndex++
            cellInfo.newIndex = newIndex
            cellListTmp.add(cellInfo)
            return newIndex
        }
        if (cellInfo.newIndex == -3) {
            // already revisited
            return cellInfo.newIndex
        }
        if (cellInfo.isSpecial) {
            // if current cell is special, previsit it first
            revisit(cellIndex, Revisit.PREVISIT)
        }
        // visit children
        for (j in cellInfo.referencesIndexes.lastIndex downTo 0) {
            revisit(cellInfo.referencesIndexes[j], Revisit.VISIT)
        }
        // allocate children
        for (j in cellInfo.referencesIndexes.lastIndex downTo 0) {
            cellInfo.referencesIndexes[j] = revisit(cellInfo.referencesIndexes[j], Revisit.ALLOCATE)
        }
        cellInfo.newIndex = -3 // mark as visited (and all children processed)
        return cellInfo.newIndex
    }

    /* TODO:
  int get_serialized_size(bool with_hashes = false) const {
return ((get_bits() + 23) >> 3) +
       (with_hashes ? get_level_mask().get_hashes_count() * (hash_bytes + depth_bytes) : 0);
 */
    private fun Cell.serializedSize(withHashes: Boolean = false): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BagOfCells) return false
        if (roots != other.roots) return false
        return true
    }

    override fun hashCode(): Int = roots.hashCode()

    private enum class Revisit {
        PREVISIT,
        VISIT,
        ALLOCATE
    }

    private data class CellInfo(
        val cell: Cell,
        var referencesIndexes: MutableList<Int> = ArrayList(),
        var hashesCount: Int = 0,
        var weight: Int = 0,
        var newIndex: Int = -1,
        var shouldCache: Boolean = false
    ) {
        val isSpecial: Boolean get() = weight == 0

        override fun toString(): String =
            "CellInfo(referencesIndexes=$referencesIndexes, hashesCount=$hashesCount, weight=$weight, newIndex=$newIndex, shouldCache=$shouldCache, isSpecial=$isSpecial, cell=${cell.bits})"
    }

    private data class RootInfo(
        val cell: Cell,
        var index: Int = -1
    )

    companion object {
        const val MAX_DEPTH = 1024
        const val MAX_CELL_WEIGHT = 64
    }
}