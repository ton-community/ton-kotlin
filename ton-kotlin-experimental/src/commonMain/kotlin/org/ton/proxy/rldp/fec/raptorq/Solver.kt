package org.ton.proxy.rldp.fec.raptorq

import org.ton.proxy.rldp.fec.raptorq.math.MatrixGF256
import org.ton.proxy.rldp.fec.raptorq.math.SparseMatrixGF2

object Solver {
    operator fun invoke(p: Params, symbols: Array<Symbol>): MatrixGF256 {
        var d = p.createD(symbols)
        val encodingRows = symbols.map { Tuple.fromParams(p, it.id) }
        var aUpper = SparseMatrixGF2(p.s + encodingRows.size, p.l)

        // LDPC 1
        for (i in 0 until p.b) {
            val a = 1 + i / p.s
            var b = i % p.s
            aUpper[b, i] = true

            b = (b + a) % p.s
            aUpper[b, i] = true

            b = (b + a) % p.s
            aUpper[b, i] = true
        }

        // Ident
        for (i in 0 until p.s) {
            aUpper[i, p.b + i] = true
        }

        // LDPC 2
        for (row in 0 until p.s) {
            aUpper[row, (row % p.p) + p.w] = true
            aUpper[row, ((row + 1) % p.p) + p.w] = true
        }

        // Encode
        encodingRows.forEachIndexed { index, row ->
            row.encode(p) { col ->
                aUpper[index + p.s, col] = true
            }
        }

        val inactivateDecoder = InactivateDecoder(aUpper, p.p)
        val inactivateResult = inactivateDecoder()
        val size = inactivateResult.size
        val rowPermutation = ArrayList<Int>(d.rows).let {
            it.addAll(inactivateResult.pRows)
            while (it.size < d.rows) {
                it.add(it.size)
            }
            it.toIntArray()
        }
        val colPermutation = inactivateResult.pCols.toIntArray()

        d = d.applyPermutation(rowPermutation)

        aUpper = aUpper.applyRowsPermutation(inversePermutation(rowPermutation))
        aUpper = aUpper.applyColsPermutation(inversePermutation(colPermutation))

        val e = aUpper.toDense(0, size, size, p.l - size)

        val c = MatrixGF256(aUpper.cols, d.cols)
        c.setFrom(d.getBlock(0, 0, size, d.cols), 0, 0)

        // Make U Identity matrix and calculate E and D_upper.
        for (i in 0 until size) {
            for (row in aUpper.getCols(i)) {
                if (row == i) continue
                if (row >= size) break
                e.rowAdd(row, e.getRow(i))
                d.rowAdd(row, d.getRow(i))
            }
        }

        val gLeft = aUpper.getBlock(size, 0, aUpper.rows - size, size)

        var smallAUpper = MatrixGF256(aUpper.rows - size, aUpper.cols - size)
        aUpper.getBlock(size, size, aUpper.rows - size, aUpper.cols - size).forEach { (row, col) ->
            smallAUpper[row, col] = 1
        }

        smallAUpper = smallAUpper.add(e.mul(gLeft).toGF256())

        // calculate small A lower
        var smallALower = MatrixGF256(p.h, aUpper.cols - size)
        for (i in 1..p.h) {
            smallALower[smallALower.rows - i, smallALower.cols - i] = 1
        }

        // calculate HDPC right and set it into small A lower
        val t = MatrixGF256(p.kPadded + p.s, p.kPadded + p.s - size)
        for (i in 0 until t.cols) {
            t[colPermutation[i + t.rows - t.cols], i] = 1
        }
        val hdpcRight = p.hdpcMultiply(t)
        smallALower.setFrom(hdpcRight, 0, 0)

        // ALower += hdpc(E)
        smallALower = smallALower.add(hdpcMul(p, colPermutation, e.toGF256()))

        val dUpper = MatrixGF256(size, d.cols)
        dUpper.setFrom(d.getBlock(0, 0, dUpper.rows, dUpper.cols), 0, 0)

        var smallDUpper = MatrixGF256(aUpper.rows - size, d.cols)
        smallDUpper.setFrom(d.getBlock(size, 0, smallDUpper.rows, smallDUpper.cols), 0, 0)
        smallDUpper = smallDUpper.add(dUpper.mulSparse(gLeft))

        var smallDLower = MatrixGF256(p.h, d.cols)
        smallDLower.setFrom(d.getBlock(aUpper.rows, 0, smallDLower.rows, smallDLower.cols), 0, 0)
        smallDLower = smallDLower.add(hdpcMul(p, colPermutation, dUpper))

        // combine small A
        val smallA = MatrixGF256(smallAUpper.rows + smallALower.rows, smallAUpper.cols)
        smallA.setFrom(smallAUpper, 0, 0)
        smallA.setFrom(smallALower, smallAUpper.rows, 0)

        // combine small D
        val smallD = MatrixGF256(smallDUpper.rows + smallDLower.rows, smallDUpper.cols)
        smallD.setFrom(smallDUpper, 0, 0)
        smallD.setFrom(smallDLower, smallDUpper.rows, 0)

        val smallC = smallA.gaussianElimination(smallD)
        c.setFrom(smallC.getBlock(0, 0, c.rows - size, c.cols), size, 0)
        for (row in 0 until size) {
            for (col in aUpper.getRows(row)) {
                if (col == row) continue
                c.rowAdd(row, c.getRow(col))
            }
        }

        return c.applyPermutation(inversePermutation(colPermutation))
    }

    private fun hdpcMul(p: Params, colPermutation: IntArray, m: MatrixGF256): MatrixGF256 {
        val t = MatrixGF256(p.kPadded + p.s, m.cols)
        for (i in 0 until m.rows) {
            t.rowSet(colPermutation[i], m.getRow(i))
        }
        return p.hdpcMultiply(t)
    }

    private fun inversePermutation(permutation: IntArray): IntArray {
        val result = IntArray(permutation.size)
        permutation.forEachIndexed { index, value ->
            result[value] = index
        }
        return result
    }
}
