package org.ton.proxy.rldp.fec.raptorq.math

import kotlin.experimental.xor

class MatrixGF2(
    val data: Array<ByteArray>
) {
    constructor(rows: Int, cols: Int) : this(Array(rows) { ByteArray(cols) })

    val rows get() = data.size
    val cols get() = data[0].size

    fun rowAdd(rows: Int, value: ByteArray) {
        for (i in 0 until cols) {
            data[rows][i] = (data[rows][i] xor value[i])
        }
    }

    operator fun set(row: Int, col: Int, value: Boolean) {
        data[row][col] = if (value) 1 else 0
    }

    operator fun get(row: Int, col: Int): Boolean =
        data[row][col] > 0.toByte()

    fun getRow(row: Int) = data[row]

    fun mul(s: SparseMatrixGF2): MatrixGF2 {
        val result = MatrixGF2(s.rows, cols)
        s.forEach { (row, col) ->
            result.rowAdd(row, getRow(col))
        }
        return result
    }

    fun toGF256(): MatrixGF256 {
        val result = MatrixGF256(rows, cols)
        for (i in data.indices) {
            result.data[i] = GF256(data[i])
        }
        return result
    }
}
