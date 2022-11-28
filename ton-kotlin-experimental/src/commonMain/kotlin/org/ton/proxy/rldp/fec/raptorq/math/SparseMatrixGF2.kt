package org.ton.proxy.rldp.fec.raptorq.math

class SparseMatrixGF2(
    val rows: Int,
    val cols: Int,
    val data: MutableSet<Long> = HashSet()
) : Iterable<Pair<Int, Int>> {
    val size get() = data.size

    operator fun set(row: Int, col: Int, value: Boolean) {
        if (value) {
            data.add((row.toLong() shl 32) or col.toLong())
        } else {
            data.remove((row.toLong() shl 32) or col.toLong())
        }
    }

    override fun iterator(): Iterator<Pair<Int, Int>> = object : Iterator<Pair<Int, Int>> {
        val iterator = data.iterator()

        override fun hasNext(): Boolean = iterator.hasNext()

        override fun next(): Pair<Int, Int> {
            val value = iterator.next()
            return Pair((value shr 32).toInt(), value.toInt())
        }
    }

    fun getCols(row: Int) =
        data.asSequence().filter { (it shr 32).toInt() == row }.map { it.toInt() }.sorted()

    fun getRows(col: Int) =
        data.asSequence().filter { it.toInt() == col }.map { (it shr 32).toInt() }.sorted()

    fun applyRowsPermutation(permutation: IntArray): SparseMatrixGF2 {
        val result = SparseMatrixGF2(rows, cols)
        forEach { (row, col) ->
            result[permutation[row], col] = true
        }
        return result
    }

    fun applyColsPermutation(permutation: IntArray): SparseMatrixGF2 {
        val result = SparseMatrixGF2(rows, cols)
        forEach { (row, col) ->
            result[row, permutation[col]] = true
        }
        return result
    }

    fun toDense(rowFrom: Int, colFrom: Int, rowSize: Int, colSize: Int): MatrixGF256 {
        val result = MatrixGF256(rowSize, colSize)
        forEach { (row, col) ->
            if (row in rowFrom until rowFrom + rowSize && col in colFrom until colFrom + colSize) {
                result[row - rowFrom, col - colFrom] = 1.toByte()
            }
        }
        return result
    }

    fun getBlock(rowOffset: Int, colOffset: Int, rowSize: Int, colSize: Int): SparseMatrixGF2 {
        val result = SparseMatrixGF2(rowSize, colSize)
        forEach { (row, col) ->
            if (row in rowOffset until rowOffset + rowSize && col in colOffset until colOffset + colSize) {
                result[row - rowOffset, col - colOffset] = true
            }
        }
        return result
    }

    companion object {
        @JvmStatic
        fun inversePermutation(permutation: IntArray): IntArray {
            val result = IntArray(permutation.size)
            permutation.forEachIndexed { index, value ->
                result[value] = index
            }
            return result
        }
    }
}
