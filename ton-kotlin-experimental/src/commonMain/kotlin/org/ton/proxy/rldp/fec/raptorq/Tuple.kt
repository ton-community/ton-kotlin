package org.ton.proxy.rldp.fec.raptorq

class Tuple(
    var d: Long,
    var a: Long,
    var b: Long,
    var d1: Long,
    var a1: Long,
    var b1: Long
) {
    fun encode(p: Params, block: (col: Int) -> Unit) {
        block(b.toInt())
        for (j in 1 until d) {
            b = (b + a) % p.w
            block(b.toInt())
        }

        while (b1 >= p.p) {
            b1 = (b1 + a1) % p.p1
        }

        block(p.w + b1.toInt())
        for (j in 1 until d1) {
            b1 = (b1 + a1) % p.p1
            while (b1 >= p.p) {
                b1 = (b1 + a1) % p.p1
            }
            block(p.w + b1.toInt())
        }
    }

    companion object {
        @JvmStatic
        fun fromParams(p: Params, x: Int): Tuple {
            var ja = 53591L + p.j * 997L
            if (ja % 2 == 0L) ja++

            val bLocal = 10267L * (p.j + 1L)
            val y = bLocal + x * ja
            val v = rand(y, 0, 1 shl 20).toInt()
            val d = deg(v, p.w)
            val a = 1L + rand(y, 1, p.w - 1L)
            val b = rand(y, 2, p.w.toLong())
            val d1 =
                if (d < 4) 2L + rand(x.toLong(), 3, 2)
                else 2L
            val a1 = 1L + rand(x.toLong(), 4, p.p1 - 1L)
            val b1 = rand(x.toLong(), 5, p.p1.toLong())
            return Tuple(d, a, b, d1, a1, b1)
        }
    }
}
