package org.ton.proxy.rldp.fec.raptorq.math

class MatrixGF256(
    val data: Array<GF256>
) {
    constructor(rows: Int, columns: Int) : this(Array(rows) { GF256(columns) })

    val rows get() = data.size
    val cols get() = data[0].data.size

    fun rowMul(row: Int, x: Octet) =
        data[row].mul(x)

    fun rowAdd(row: Int, other: GF256) =
        data[row].add(other)

    fun rowAddMul(row: Int, other: GF256, x: Octet) {
        if (x.value == 0.toUByte()) return
        if (x.value == 1.toUByte()) {
            rowAdd(row, other)
            return
        }
        data[row].addMul(other, x)
    }

    operator fun set(row: Int, column: Int, value: Byte) {
        data[row].data[column] = value
    }

    operator fun get(row: Int, column: Int): Byte =
        data[row].data[column]

    fun rowSet(row: Int, other: GF256) {
        val gf = GF256(other.data.copyOf())
        data[row] = gf
    }

    fun setFrom(g: MatrixGF256, rowOffset: Int, columnOffset: Int) {
        g.data.forEachIndexed { index, row ->
            row.data.copyInto(data[index + rowOffset].data, columnOffset)
        }
    }

    fun getRow(row: Int): GF256 =
        GF256(data[row].data)

    fun getBlock(rowOffset: Int, colOffset: Int, rowSize: Int, colSize: Int): MatrixGF256 {
        val block = MatrixGF256(rowSize, colSize)
        for (row in rowOffset until rowSize + rowOffset) {
            val col = ByteArray(colSize)
            data[row].data.copyInto(col, startIndex = colOffset)
            block.data[row - rowOffset] = GF256(col)
        }
        return block
    }

    fun applyPermutation(permutation: IntArray): MatrixGF256 {
        val result = MatrixGF256(rows, cols)
        for (row in 0 until rows) {
            result.data[row] = data[permutation[row]]
        }
        return result
    }

    fun mulSparse(s: SparseMatrixGF2): MatrixGF256 {
        val result = MatrixGF256(s.rows, cols)
        s.forEach { (row, col) ->
            result.rowAdd(row, data[col])
        }
        return result
    }

    fun add(s: MatrixGF256): MatrixGF256 {
        val result = copy()
        for (i in 0 until rows) {
            result.rowAdd(i, s.data[i])
        }
        return result
    }

    fun copy(): MatrixGF256 {
        val result = MatrixGF256(rows, cols)
        data.copyInto(result.data)
        return result
    }

    fun gaussianElimination(d: MatrixGF256): MatrixGF256 {
        val a = copy()
        val d = d.copy()
        val rows = a.rows

        val rowPerm = IntArray(rows) { it }

        for (row in 0 until a.cols) {
            var nonZero = row
            while (nonZero < rows && a[rowPerm[nonZero], row] == 0.toByte()) {
                nonZero++
            }
            check(nonZero != rows) { "Matrix is not solvable" }

            if (nonZero != row) {
                val tmp = rowPerm[nonZero]
                rowPerm[nonZero] = rowPerm[row]
                rowPerm[row] = tmp
            }

            val mul = a[rowPerm[row], row].toOctet().inv()

            a.rowMul(rowPerm[row], mul)
            d.rowMul(rowPerm[row], mul)

            for (zeroRow in 0 until rows) {
                if (zeroRow == row) continue
                val x = a[rowPerm[zeroRow], row].toOctet()
                if (x.value != 0.toUByte()) {
                    a.rowAddMul(rowPerm[zeroRow], a.getRow(rowPerm[row]), x)
                    d.rowAddMul(rowPerm[zeroRow], d.getRow(rowPerm[row]), x)
                }
            }
        }

        return d.applyPermutation(rowPerm)
    }
}
