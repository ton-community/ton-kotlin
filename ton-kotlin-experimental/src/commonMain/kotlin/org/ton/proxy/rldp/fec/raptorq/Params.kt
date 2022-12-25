package org.ton.proxy.rldp.fec.raptorq

import org.ton.proxy.rldp.fec.raptorq.math.MatrixGF256
import org.ton.proxy.rldp.fec.raptorq.math.Octet

class Params(
    val k: Int,
    val kPadded: Int,
    val j: Int,
    val s: Int,
    val h: Int,
    val w: Int,
    val l: Int,
    val p: Int,
    val p1: Int,
    val u: Int,
    val b: Int
) {
    fun genSymbol(relaxed: MatrixGF256, symbolSize: Int, id: Int): ByteArray {
        val m = MatrixGF256(1, symbolSize)
        val tuple = Tuple.fromParams(this, id)
        tuple.encode(this) { col ->
            m.rowAdd(0, relaxed.getRow(col))
        }
        return m.getRow(0).data
    }

    fun createD(symbols: Array<Symbol>): MatrixGF256 {
        val symbolSize = symbols[0].data.size
        val d = MatrixGF256(s + h + symbols.size, symbolSize)
        var offset = s
        for (symbol in symbols) {
            symbol.data.forEachIndexed { index, byte ->
                d[offset, index] = byte
            }
            offset++
        }
        return d
    }

    fun hdpcMultiply(v: MatrixGF256): MatrixGF256 {
        val alpha = Octet(1u).exp()
        for (i in 1 until v.rows) {
            v.rowAddMul(i, v.getRow(i - 1), alpha)
        }

        val u = MatrixGF256(h, v.cols)
        for (i in 0 until h) {
            val exp = Octet((i % 255).toUByte()).exp()
            val row = v.getRow(v.rows - 1)
            u.rowAddMul(i, row, exp)
        }

        for (col in 0 until v.rows - 1) {
            val a = rand(col + 1L, 6, h.toLong()).toInt()
            val b = ((a + rand(col + 1L, 7, h - 1L) + 1) % h).toInt()
            u.rowAdd(a, v.getRow(col))
            u.rowAdd(b, v.getRow(col))
        }
        return u
    }

    companion object {
        fun of(dataSize: Int, symbolSize: Int): Params {
            val k = (dataSize + symbolSize - 1) / symbolSize
            val raw = RawParams.fromK(k)
            val l = raw.kPadded + raw.s + raw.h
            val b = raw.w - raw.s
            val p = l - raw.w
            val u = p - raw.h
            var p1 = p + 1
            while (!isPrime(p1)) {
                p1++
            }
            return Params(
                k = k,
                kPadded = raw.kPadded,
                j = raw.j,
                s = raw.s,
                h = raw.h,
                w = raw.w,
                l = l,
                p = p,
                p1 = p1,
                u = u,
                b = b
            )
        }

        // fast is prime algorithm
        private fun isPrime(int: Int): Boolean {
            if (int <= 1) return false
            if (int <= 3) return true
            if (int % 2 == 0 || int % 3 == 0) return false
            var i = 5
            while (i * i <= int) {
                if (int % i == 0 || int % (i + 2) == 0) return false
                i += 6
            }
            return true
        }
    }
}
