@file:OptIn(ExperimentalUnsignedTypes::class)

package ton.crypto

private val K = longArrayOf(
    0x428a2f98, 0xd728ae22, 0x71374491, 0x23ef65cd,
    0xb5c0fbcf, 0xec4d3b2f, 0xe9b5dba5, 0x8189dbbc,
    0x3956c25b, 0xf348b538, 0x59f111f1, 0xb605d019,
    0x923f82a4, 0xaf194f9b, 0xab1c5ed5, 0xda6d8118,
    0xd807aa98, 0xa3030242, 0x12835b01, 0x45706fbe,
    0x243185be, 0x4ee4b28c, 0x550c7dc3, 0xd5ffb4e2,
    0x72be5d74, 0xf27b896f, 0x80deb1fe, 0x3b1696b1,
    0x9bdc06a7, 0x25c71235, 0xc19bf174, 0xcf692694,
    0xe49b69c1, 0x9ef14ad2, 0xefbe4786, 0x384f25e3,
    0x0fc19dc6, 0x8b8cd5b5, 0x240ca1cc, 0x77ac9c65,
    0x2de92c6f, 0x592b0275, 0x4a7484aa, 0x6ea6e483,
    0x5cb0a9dc, 0xbd41fbd4, 0x76f988da, 0x831153b5,
    0x983e5152, 0xee66dfab, 0xa831c66d, 0x2db43210,
    0xb00327c8, 0x98fb213f, 0xbf597fc7, 0xbeef0ee4,
    0xc6e00bf3, 0x3da88fc2, 0xd5a79147, 0x930aa725,
    0x06ca6351, 0xe003826f, 0x14292967, 0x0a0e6e70,
    0x27b70a85, 0x46d22ffc, 0x2e1b2138, 0x5c26c926,
    0x4d2c6dfc, 0x5ac42aed, 0x53380d13, 0x9d95b3df,
    0x650a7354, 0x8baf63de, 0x766a0abb, 0x3c77b2a8,
    0x81c2c92e, 0x47edaee6, 0x92722c85, 0x1482353b,
    0xa2bfe8a1, 0x4cf10364, 0xa81a664b, 0xbc423001,
    0xc24b8b70, 0xd0f89791, 0xc76c51a3, 0x0654be30,
    0xd192e819, 0xd6ef5218, 0xd6990624, 0x5565a910,
    0xf40e3585, 0x5771202a, 0x106aa070, 0x32bbd1b8,
    0x19a4c116, 0xb8d2d0c8, 0x1e376c08, 0x5141ab53,
    0x2748774c, 0xdf8eeb99, 0x34b0bcb5, 0xe19b48a8,
    0x391c0cb3, 0xc5c95a63, 0x4ed8aa4a, 0xe3418acb,
    0x5b9cca4f, 0x7763e373, 0x682e6ff3, 0xd6b2b8a3,
    0x748f82ee, 0x5defb2fc, 0x78a5636f, 0x43172f60,
    0x84c87814, 0xa1f0ab72, 0x8cc70208, 0x1a6439ec,
    0x90befffa, 0x23631e28, 0xa4506ceb, 0xde82bde9,
    0xbef9a3f7, 0xb2c67915, 0xc67178f2, 0xe372532b,
    0xca273ece, 0xea26619c, 0xd186b8c7, 0x21c0c207,
    0xeada7dd6, 0xcde0eb1e, 0xf57d4f7f, 0xee6ed178,
    0x06f067aa, 0x72176fba, 0x0a637dc5, 0xa2c898a6,
    0x113f9804, 0xbef90dae, 0x1b710b35, 0x131c471b,
    0x28db77f5, 0x23047d84, 0x32caab7b, 0x40c72493,
    0x3c9ebe0a, 0x15c9bebc, 0x431d67c4, 0x9c100d4c,
    0x4cc5d4be, 0xcb3e42b6, 0x597f299c, 0xfc657e2a,
    0x5fcb6fab, 0x3ad6faec, 0x6c44198c, 0x4a475817
)

fun sha512(byteArray: ByteArray): ByteArray {
    val hash = IntArray(64)
    sha512(hash, byteArray.toIntArray(), byteArray.size)
    byteArray.toUByteArray()
    return hash.toByteArray()
}

internal fun sha512(out: IntArray, m: IntArray, _n: Int) {
    val hh = IntArray(8)
    val hl = IntArray(8)
    val x = IntArray(256)
    var n = _n
    val b = n

    hh[0] = 0x6a09e667
    hh[1] = 0xbb67ae85.toInt()
    hh[2] = 0x3c6ef372
    hh[3] = 0xa54ff53a.toInt()
    hh[4] = 0x510e527f
    hh[5] = 0x9b05688c.toInt()
    hh[6] = 0x1f83d9ab
    hh[7] = 0x5be0cd19

    hl[0] = 0xf3bcc908.toInt()
    hl[1] = 0x84caa73b.toInt()
    hl[2] = 0xfe94f82b.toInt()
    hl[3] = 0x5f1d36f1
    hl[4] = 0xade682d1.toInt()
    hl[5] = 0x2b3e6c1f
    hl[6] = 0xfb41bd6b.toInt()
    hl[7] = 0x137e2179

    crypto_hashblocks_hl(hh, hl, m, n)
    n %= 128

    for (i in 0 until n) {
        x[i] = m[b - n + i]
    }
    x[n] = 128

    // *** R n = 256-128 * (n<112?1:0);
    n = if (n < 112) 128 else 256
    x[n - 9] = 0
    ts64(x, n - 8, ((b / 0x20000000) or 0), (b shl 3))
    crypto_hashblocks_hl(hh, hl, x, n)

    ts64(out, 0, hh[0], hl[0])
    ts64(out, 8, hh[1], hl[1])
    ts64(out, 16, hh[2], hl[2])
    ts64(out, 24, hh[3], hl[3])
    ts64(out, 32, hh[4], hl[4])
    ts64(out, 40, hh[5], hl[5])
    ts64(out, 48, hh[6], hl[6])
    ts64(out, 56, hh[7], hl[7])
}

private fun ts64(x: IntArray, i: Int, h: Int, l: Int) {
    x[i] = ((h shr 24) and 0xff)
    x[i + 1] = ((h shr 16) and 0xff)
    x[i + 2] = ((h shr 8) and 0xff)
    x[i + 3] = (h and 0xff)
    x[i + 4] = ((l shr 24) and 0xff)
    x[i + 5] = ((l shr 16) and 0xff)
    x[i + 6] = ((l shr 8) and 0xff)
    x[i + 7] = (l and 0xff)
}

private fun crypto_hashblocks_hl(hh: IntArray, hl: IntArray, m: IntArray, _n: Int): Int {
    val wh = IntArray(16)
    val wl = IntArray(16)

    val bh = IntArray(8)
    val bl = IntArray(8)

    var th: Int
    var tl: Int
    var h: Int
    var l: Int
    var a: Int
    var b: Int
    var c: Int
    var d: Int

    val ah = IntArray(8)
    val al = IntArray(8)
    hh.copyInto(ah, endIndex = 8)
    hl.copyInto(al, endIndex = 8)

    var pos = 0
    var n = _n
    while (n >= 128) {

        for (i in 0..15) {
            val j = 8 * i + pos
            wh[i] = (m[j + 0] shl 24) or (m[j + 1] shl 16) or (m[j + 2] shl 8) or m[j + 3]
            wl[i] = (m[j + 4] shl 24) or (m[j + 5] shl 16) or (m[j + 6] shl 8) or m[j + 7]
        }

        for (i in 0..79) {
            ah.copyInto(bh, endIndex = 7)
            al.copyInto(bl, endIndex = 7)

            // add
            h = ah[7]
            l = al[7]

            a = l and 0xffff; b = l ushr 16
            c = h and 0xffff; d = h ushr 16

            // Sigma1
            h =
                ((ah[4] ushr 14) or (al[4] shl (32 - 14))) xor ((ah[4] ushr 18) or (al[4] shl (32 - 18))) xor ((al[4] ushr (41 - 32)) or (ah[4] shl (32 - (41 - 32))))
            l =
                ((al[4] ushr 14) or (ah[4] shl (32 - 14))) xor ((al[4] ushr 18) or (ah[4] shl (32 - 18))) xor ((ah[4] ushr (41 - 32)) or (al[4] shl (32 - (41 - 32))))

            a += l and 0xffff
            b += l ushr 16
            c += h and 0xffff
            d += h ushr 16

            // Ch
            h = (ah[4] and ah[5]) xor (ah[4].inv() and ah[6])
            l = (al[4] and al[5]) xor (al[4].inv() and al[6])

            a += l and 0xffff; b += l ushr 16
            c += h and 0xffff; d += h ushr 16

            // K
            h = K[i * 2].toInt()
            l = K[i * 2 + 1].toInt()

            a += l and 0xffff
            b += l ushr 16
            c += h and 0xffff
            d += h ushr 16

            // w
            h = wh[i % 16]
            l = wl[i % 16]

            a += l and 0xffff
            b += l ushr 16
            c += h and 0xffff
            d += h ushr 16

            b += a ushr 16
            c += b ushr 16
            d += c ushr 16

            // *** R
            th = c and 0xffff or (d shl 16)
            tl = a and 0xffff or (b shl 16)

            // add
            h = th
            l = tl

            a = l and 0xffff
            b = l ushr 16
            c = h and 0xffff
            d = h ushr 16

            // Sigma0
            h =
                ((ah[0] ushr 28) or (al[0] shl (32 - 28))) xor ((al[0] ushr (34 - 32)) or (ah[0] shl (32 - (34 - 32)))) xor ((al[0] ushr (39 - 32)) or (ah[0] shl (32 - (39 - 32))))
            l =
                ((al[0] ushr 28) or (ah[0] shl (32 - 28))) xor ((ah[0] ushr (34 - 32)) or (al[0] shl (32 - (34 - 32)))) xor ((ah[0] ushr (39 - 32)) or (al[0] shl (32 - (39 - 32))))

            a += l and 0xffff
            b += l ushr 16
            c += h and 0xffff
            d += h ushr 16

            // Maj
            h = (ah[0] and ah[1]) xor (ah[0] and ah[2]) xor (ah[1] and ah[2])
            l = (al[0] and al[1]) xor (al[0] and al[2]) xor (al[1] and al[2])

            a += l and 0xffff; b += l ushr 16
            c += h and 0xffff; d += h ushr 16

            b += a ushr 16
            c += b ushr 16
            d += c ushr 16

            bh[7] = (c and 0xffff) or (d shl 16)
            bl[7] = (a and 0xffff) or (b shl 16)

            // add
            h = bh[3]
            l = bl[3]

            a = l and 0xffff
            b = l ushr 16
            c = h and 0xffff
            d = h ushr 16

            h = th
            l = tl

            a += l and 0xffff
            b += l ushr 16
            c += h and 0xffff
            d += h ushr 16

            b += a ushr 16
            c += b ushr 16
            d += c ushr 16

            bh[3] = (c and 0xffff) or (d shl 16)
            bl[3] = (a and 0xffff) or (b shl 16)

            for (j in 0..7) {
                val k = (j + 1) % 8
                ah[k] = bh[j]
                al[k] = bl[j]
            }

            if (i % 16 == 15) {
                for (j in 0..15) {
                    // add
                    h = wh[j]
                    l = wl[j]

                    a = l and 0xffff; b = l ushr 16
                    c = h and 0xffff; d = h ushr 16

                    h = wh[(j + 9) % 16]
                    l = wl[(j + 9) % 16]

                    a += l and 0xffff; b += l ushr 16
                    c += h and 0xffff; d += h ushr 16

                    // sigma0
                    th = wh[(j + 1) % 16]
                    tl = wl[(j + 1) % 16]

                    h = ((th ushr 1) or (tl shl (32 - 1))) xor ((th ushr 8) or (tl shl (32 - 8))) xor (th ushr 7)
                    l =
                        ((tl ushr 1) or (th shl (32 - 1))) xor ((tl ushr 8) or (th shl (32 - 8))) xor ((tl ushr 7) or (th shl (32 - 7)))

                    a += l and 0xffff; b += l ushr 16
                    c += h and 0xffff; d += h ushr 16

                    // sigma1
                    th = wh[(j + 14) % 16]
                    tl = wl[(j + 14) % 16]

                    h =
                        ((th ushr 19) or (tl shl (32 - 19))) xor ((tl ushr (61 - 32)) or (th shl (32 - (61 - 32)))) xor (th ushr 6)
                    l =
                        ((tl ushr 19) or (th shl (32 - 19))) xor ((th ushr (61 - 32)) or (tl shl (32 - (61 - 32)))) xor ((tl ushr 6) or (th shl (32 - 6)))

                    a += l and 0xffff; b += l ushr 16
                    c += h and 0xffff; d += h ushr 16

                    b += a ushr 16
                    c += b ushr 16
                    d += c ushr 16

                    wh[j] = ((c and 0xffff) or (d shl 16))
                    wl[j] = ((a and 0xffff) or (b shl 16))
                }
            }
        }

        // add
        a = 0; b = 0; c = 0; d = 0
        for (k in 0..7) {
            if (k == 0) {
                h = ah[0]
                l = al[0]
                a = l and 0xffff; b = l ushr 16
                c = h and 0xffff; d = h ushr 16
            }

            h = hh[k]
            l = hl[k]

            a += l and 0xffff; b += l ushr 16
            c += h and 0xffff; d += h ushr 16

            b += a ushr 16
            c += b ushr 16
            d += c ushr 16

            hh[k] = (c and 0xffff) or (d shl 16)
            ah[k] = (c and 0xffff) or (d shl 16)

            hl[k] = (a and 0xffff) or (b shl 16)
            al[k] = (a and 0xffff) or (b shl 16)

            if (k < 7) {
                h = ah[k + 1]
                l = al[k + 1]

                a = l and 0xffff; b = l ushr 16
                c = h and 0xffff; d = h ushr 16
            }
        }

        pos += 128
        n -= 128
    }

    return n
}