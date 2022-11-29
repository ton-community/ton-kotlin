package org.ton.proxy.rldp.fec.raptorq

import org.ton.proxy.rldp.fec.raptorq.math.SparseMatrixGF2
import kotlin.math.min

class InactivateDecoder(
    val l: SparseMatrixGF2,
    val pi: Int,
    private val cols: Int = l.cols - pi,
    private val rows: Int = l.rows,
    private val wasRow: BooleanArray = BooleanArray(rows),
    private val wasCol: BooleanArray = BooleanArray(cols),
    private val colCnt: IntArray = IntArray(cols),
    private val rowCnt: IntArray = IntArray(rows),
    private val rowXor: IntArray = IntArray(rows),
) {
    private val rowCntOffset = IntArray(rows)
    private val sortedRows = IntArray(rows)
    private val rowPos = IntArray(rows)
    private val pCols = ArrayList<Int>()
    private val pRows = ArrayList<Int>()
    private val inactivateCols = ArrayList<Int>()

    operator fun invoke(): InactivationDecodingResult {
        l.forEach { (row, col) ->
            if (col >= cols) return@forEach
            colCnt[col]++
            rowCnt[row]++
            rowXor[row] = rowXor[row] xor col
        }
        sort()
        loop()

        for (row in 0 until rows) {
            if (!wasRow[row]) {
                pRows.add(row)
            }
        }

        val size = pCols.size
        for (col in inactivateCols.asReversed()) {
            pCols.add(col)
        }
        for (i in 0 until pi) {
            pCols.add(cols + i)
        }
        return InactivationDecodingResult(size, pRows, pCols)
    }

    fun sort() {
        val offset = IntArray(cols + 2)
        for (i in 0 until rows) {
            offset[rowCnt[i] + 1]++
        }
        for (i in 1..cols + 1) {
            offset[i] += offset[i - 1]
        }
        offset.copyInto(rowCntOffset, endIndex = min(rowCntOffset.size, offset.size))
        for (i in 0 until rows) {
            val pos = offset[rowCnt[i]]
            offset[rowCnt[i]]++
            sortedRows[pos] = i
            rowPos[i] = pos
        }
    }

    fun loop() {
        while (rowCntOffset[1] != rows) {
            val row = sortedRows[rowCntOffset[1]]
            val col = chooseCol(row)

            val cnt = rowCnt[row]
            pCols.add(col)
            pRows.add(row)

            if (cnt == 1) {
                inactivate(col)
            } else {
                val rows = l.getRows(row).toList()
                for (x in rows) {
                    if (x >= cols || wasCol[x]) continue
                    if (x != col) {
                        inactivateCols.add(x)
                    }
                    inactivate(x)
                }
            }
            wasRow[row] = true
        }
    }

    fun chooseCol(row: Int): Int {
        val cnt = rowCnt[row]
        if (cnt == 1) {
            return rowXor[row]
        }
        var bestCol = Int.MIN_VALUE
        for (col in l.getRows(row)) {
            if (col >= cols || wasCol[col]) continue
            if (bestCol == Int.MIN_VALUE || colCnt[col] < colCnt[bestCol]) {
                bestCol = col
            }
        }
        return bestCol
    }

    fun inactivate(col: Int) {
        wasCol[col] = true
        val cols = l.getCols(col).toList()
        for (row in cols) {
            if (wasRow[row]) continue

            val pos = rowPos[row]
            val cnt = rowCnt[row]
            val offset = rowCntOffset[cnt]

            val tmp = sortedRows[pos]
            sortedRows[pos] = sortedRows[offset]
            sortedRows[offset] = tmp

            rowPos[sortedRows[pos]] = pos
            rowPos[sortedRows[offset]] = offset
            rowCntOffset[cnt]++
            rowCnt[row]--
            rowXor[row] = rowXor[row] xor col
        }
    }
}

data class InactivationDecodingResult(
    val size: Int,
    val pRows: MutableList<Int>,
    val pCols: MutableList<Int>
)
