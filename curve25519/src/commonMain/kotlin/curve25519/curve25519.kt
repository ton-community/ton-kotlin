// Curve25519 signatures (and also key agreement)
// like in the early Axolotl.
//
// Ported to Kotlin by Miguel Sandro Lucero. miguel.sandro@gmail.com. 2017.05.31
// You can use it under MIT or CC0 license.
//
// Curve25519 signatures idea and math by Trevor Perrin
// https://moderncrypto.org/mail-archive/curves/2014/000205.html
//
// Derived from axlsign.js written by Dmitry Chestnykh. https://github.com/wavesplatform/curve25519-js

@file:Suppress("NOTHING_TO_INLINE")

package curve25519

import kotlin.math.floor
import kotlin.random.Random

object Curve25519 {
    const val HIGH = 255
    const val LOW = 0

    fun sharedKey(secretKey: IntArray, publicKey: IntArray): IntArray {
        val sharedKey = IntArray(32)
        crypto_scalarmult(sharedKey, secretKey, publicKey)
        return sharedKey
    }

    fun signMessage(secretKey: IntArray, msg: IntArray, opt_random: IntArray? = null): IntArray {
        return if (opt_random != null) {
            val buf = IntArray(128 + msg.size)
            curve25519_sign(buf, msg, msg.size, secretKey, opt_random)
            buf.copyOfRange(0, 64 + msg.size)
        } else {
            val signedMsg = IntArray(64 + msg.size)
            curve25519_sign(signedMsg, msg, msg.size, secretKey, null)
            signedMsg
        }
    }

    // add by Miguel
    fun openMessageStr(publicKey: IntArray, signedMsg: IntArray): String {
        val m = openMessage(publicKey, signedMsg)
        return if (m != null) {
            buildString {
                for (element in m) {
                    append(element.toChar())
                }
            }
        } else {
            ""
        }
    }

    fun openMessage(publicKey: IntArray, signedMsg: IntArray): IntArray? {
        val tmp = IntArray(signedMsg.size)
        val mlen = curve25519_sign_open(tmp, signedMsg, signedMsg.size, publicKey)
        if (mlen < 0) {
            return null
        }
        val m = IntArray(mlen)
        for (i in m.indices) {
            m[i] = tmp[i]
        }
        return m
    }

    fun sign(secretKey: IntArray, msg: IntArray, opt_random: IntArray?): IntArray {
        var len = 64
        if (opt_random != null) {
            len = 128
        }
        val buf = IntArray(len + msg.size)
        curve25519_sign(buf, msg, msg.size, secretKey, opt_random)

        val signature = IntArray(64)
        buf.copyInto(signature, endIndex = signature.size)
        return signature
    }

    fun verify(publicKey: IntArray, msg: IntArray, signature: IntArray): Boolean {
        val sm = IntArray(64 + msg.size)
        val m = IntArray(64 + msg.size)

        signature.copyInto(sm, endIndex = 64)

        for (i in msg.indices) {
            sm[i + 64] = msg[i]
        }

        return curve25519_sign_open(m, sm, sm.size, publicKey) >= 0
    }

    fun generateKeyPair(seed: IntArray): KeyPair {
        val sk = IntArray(32)
        val pk = IntArray(32)

        seed.copyInto(sk, endIndex = 32)

        crypto_scalarmult(pk, sk)

        // Turn secret key into the correct format.
        sk[0] = sk[0] and 248
        sk[31] = sk[31] and 127
        sk[31] = sk[31] or 64

        // Remove sign bit from public key.
        pk[31] = pk[31] and 127

        return KeyPair(pk, sk)
    }

    fun randomBytes(size: Int): IntArray {
        val seed = IntArray(size)
        for (i in seed.indices) {
            seed[i] = Random.nextInt(HIGH - LOW) + LOW
        }
        return seed
    }

    data class KeyPair(val publicKey: IntArray, val privateKey: IntArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as KeyPair

            if (!publicKey.contentEquals(other.publicKey)) return false
            if (!privateKey.contentEquals(other.privateKey)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = publicKey.contentHashCode()
            result = 31 * result + privateKey.contentHashCode()
            return result
        }
    }
}

// *** R val _0 = IntArray(16)
private val _9 = intArrayOf(
    0x9, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
    0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
    0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
    0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0)

private val gf0 = gf()

private val gf1 = gf(longArrayOf(1))

private val _121665 = gf(longArrayOf(0xdb41, 1))

private val D = gf(longArrayOf(
    0x78a3, 0x1359, 0x4dca, 0x75eb,
    0xd8ab, 0x4141, 0x0a4d, 0x0070,
    0xe898, 0x7779, 0x4079, 0x8cc7,
    0xfe73, 0x2b6f, 0x6cee, 0x5203))

private val D2 = gf(longArrayOf(
    0xf159, 0x26b2, 0x9b94, 0xebd6,
    0xb156, 0x8283, 0x149a, 0x00e0,
    0xd130, 0xeef3, 0x80f2, 0x198e,
    0xfce7, 0x56df, 0xd9dc, 0x2406))

private val X = gf(longArrayOf(
    0xd51a, 0x8f25, 0x2d60, 0xc956,
    0xa7b2, 0x9525, 0xc760, 0x692c,
    0xdc5c, 0xfdd6, 0xe231, 0xc0a4,
    0x53fe, 0xcd6e, 0x36d3, 0x2169))

private val Y = gf(longArrayOf(
    0x6658, 0x6666, 0x6666, 0x6666,
    0x6666, 0x6666, 0x6666, 0x6666,
    0x6666, 0x6666, 0x6666, 0x6666,
    0x6666, 0x6666, 0x6666, 0x6666))

private val I = gf(longArrayOf(
    0xa0b0, 0x4a0e, 0x1b27, 0xc4ee,
    0xe478, 0xad2f, 0x1806, 0x2f43,
    0xd7a7, 0x3dfb, 0x0099, 0x2b4d,
    0xdf0b, 0x4fc1, 0x2480, 0x2b83))

private inline fun floorLng(v: Double): Long = floor(v).toLong()

private inline fun gf() = LongArray(16)
private inline fun gf(init: LongArray) = LongArray(16).also { init.copyInto(it) }

private inline fun ts64(x: IntArray, i: Int, h: Int, l: Int) {
    x[i] = ((h shr 24) and 0xff)
    x[i + 1] = ((h shr 16) and 0xff)
    x[i + 2] = ((h shr 8) and 0xff)
    x[i + 3] = (h and 0xff)
    x[i + 4] = ((l shr 24) and 0xff)
    x[i + 5] = ((l shr 16) and 0xff)
    x[i + 6] = ((l shr 8) and 0xff)
    x[i + 7] = (l and 0xff)
}

private fun crypto_verify(x: IntArray, xi: Int, y: IntArray, yi: Int, n: Int = 32): Int {
    var d = 0
    for (i in 0 until n) {
        d = d or (x[xi + i] xor y[yi + i])
    }
    return (1 and ((d - 1) ushr 8)) - 1
}

private fun set25519(r: LongArray, a: LongArray) {
    for (i in 0..15) {
        r[i] = a[i] or 0
    }
}

private fun car25519(o: LongArray) {
    var v: Long
    var c = 1L
    for (i in 0..15) {
        v = o[i] + c + 65535
        c = floorLng(v / 65536.0)
        o[i] = v - c * 65536
    }
    o[0] += c - 1 + 37 * (c - 1)
}

private fun sel25519(p: LongArray, q: LongArray, b: Int) {
    var t: Long
    val invb = (b - 1).inv()
    val c: Long = invb.toLong()
    for (i in 0..15) {
        t = c and (p[i] xor q[i])
        p[i] = p[i] xor t
        q[i] = q[i] xor t
    }
}

private fun pack25519(o: IntArray, n: LongArray) {
    var b: Long
    val m = gf()
    val t = gf()

    for (i in 0..15) {
        t[i] = n[i]
    }
    car25519(t)
    car25519(t)
    car25519(t)

    for (j in 0..1) {
        m[0] = t[0] - 0xffed
        for (i in 1..14) {
            m[i] = t[i] - 0xffff - ((m[i - 1] shr 16) and 1)
            m[i - 1] = m[i - 1] and 0xffff
        }
        m[15] = t[15] - 0x7fff - ((m[14] shr 16) and 1)
        b = (m[15] shr 16) and 1
        m[14] = m[14] and 0xffff
        sel25519(t, m, 1 - b.toInt())
    }
    for (i in 0..15) {
        o[2 * i] = (t[i]).toInt() and 0xff
        o[2 * i + 1] = (t[i]).toInt() shr 8
    }
}

private fun neq25519(a: LongArray, b: LongArray): Int {
    val c = IntArray(32)
    val d = IntArray(32)
    pack25519(c, a)
    pack25519(d, b)
    return crypto_verify(c, 0, d, 0)
}

private fun par25519(a: LongArray): Int {
    val d = IntArray(32)
    pack25519(d, a)
    return d[0] and 1
}

private fun unpack25519(o: LongArray, n: IntArray) {
    for (i in 0..15) {
        val value: Int = n[2 * i] + (n[2 * i + 1] shl 8)
        o[i] = value.toLong()
    }
    o[15] = o[15] and 0x7fff
}

private fun A(o: LongArray, a: LongArray, b: LongArray) {
    for (i in 0..15) {
        o[i] = a[i] + b[i]
    }
}

private fun Z(o: LongArray, a: LongArray, b: LongArray) {
    for (i in 0..15) {
        o[i] = a[i] - b[i]
    }
}

// optimized by Miguel
private fun M(o: LongArray, a: LongArray, b: LongArray) {
    val at = LongArray(32)
    val ab = LongArray(16)

    b.copyInto(ab, endIndex = 16)

    for (i in 0..15) {
        val v = a[i]
        for (j in 0..15) {
            at[j + i] += v * ab[j]
        }
    }

    for (i in 0..14) {
        at[i] += 38 * at[i + 16]
    }
    // t15 left as is

    // first car
    var c: Long = 1
    for (i in 0..15) {
        val v = at[i] + c + 65535
        c = floorLng(v / 65536.0)
        at[i] = v - c * 65536
    }
    at[0] += c - 1 + 37 * (c - 1)

    // second car
    c = 1
    for (i in 0..15) {
        val v = at[i] + c + 65535
        c = floorLng(v / 65536.0)
        at[i] = v - c * 65536
    }
    at[0] += c - 1 + 37 * (c - 1)

    at.copyInto(o, endIndex = 16)
}

private fun S(o: LongArray, a: LongArray) = M(o, a, a)

private fun inv25519(o: LongArray, i: LongArray) {
    val c = gf()
    for (a in 0..15) {
        c[a] = i[a]
    }

    for (a in 253 downTo 0) {
        S(c, c)
        if (a != 2 && a != 4) {
            M(c, c, i)
        }
    }
    for (a in 0..15) {
        o[a] = c[a]
    }
}

private fun pow2523(o: LongArray, i: LongArray) {
    val c = gf()
    for (a in 0..15) {
        c[a] = i[a]
    }
    for (a in 250 downTo 0) {
        S(c, c)
        if (a != 1) {
            M(c, c, i)
        }
    }
    for (a in 0..15) {
        o[a] = c[a]
    }
}

private fun crypto_scalarmult(q: IntArray, n: IntArray, p: IntArray = _9): Int {
    val z = IntArray(32)
    val x = LongArray(80)
    var r: Int

    val a = gf()
    val b = gf()
    val c = gf()
    val d = gf()
    val e = gf()
    val f = gf()

    for (i in 0..30) {
        z[i] = n[i]
    }
    z[31] = (n[31] and 127) or 64
    z[0] = z[0] and 248

    unpack25519(x, p)

    for (i in 0..15) {
        b[i] = x[i]
        d[i] = 0
        a[i] = 0
        c[i] = 0
    }
    a[0] = 1
    d[0] = 1

    for (i in 254 downTo 0) {
        r = (z[i ushr 3] ushr (i and 7)) and 1

        sel25519(a, b, r)
        sel25519(c, d, r)
        A(e, a, c)
        Z(a, a, c)
        A(c, b, d)
        Z(b, b, d)
        S(d, e)
        S(f, a)
        M(a, c, a)
        M(c, b, e)
        A(e, a, c)
        Z(a, a, c)
        S(b, a)
        Z(c, d, f)
        M(a, c, _121665)
        A(a, a, d)
        M(c, c, a)
        M(a, d, f)
        M(d, b, x)
        S(b, e)
        sel25519(a, b, r)
        sel25519(c, d, r)
    }

    for (i in 0..15) {
        x[i + 16] = a[i]
        x[i + 32] = c[i]
        x[i + 48] = b[i]
        x[i + 64] = d[i]
    }

    val x32 = x.copyOfRange(32, x.size) // from 32
    val x16 = x.copyOfRange(16, x.size) // from 16

    inv25519(x32, x32)
    M(x16, x16, x32)
    pack25519(q, x16)

    return 0
}

// Constantes de cada ronda del SHA-512
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

// optimized by miguel
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

private val _HH =
    longArrayOf(0x6a09e667, 0xbb67ae85, 0x3c6ef372, 0xa54ff53a, 0x510e527f, 0x9b05688c, 0x1f83d9ab, 0x5be0cd19)
private val _HL =
    longArrayOf(0xf3bcc908, 0x84caa73b, 0xfe94f82b, 0x5f1d36f1, 0xade682d1, 0x2b3e6c1f, 0xfb41bd6b, 0x137e2179)

private fun LongArray.toIntArray(): IntArray {
    val v = IntArray(this.size)
    for (i in 0 until this.size) {
        v[i] = this[i].toInt()
    }
    return v
}

private fun crypto_hash(out: IntArray, m: IntArray, _n: Int) {
    val hh = _HH.toIntArray()
    val hl = _HL.toIntArray()
    val x = IntArray(256)
    var n = _n
    val b = n

    crypto_hashblocks_hl(hh, hl, m, n)

    n %= 128

    for (i in 0 until n) {
        x[i] = m[b - n + i]
    }
    x[n] = 128

    // *** R n = 256-128 * (n<112?1:0);
    n = if (n < 112) {
        128
    } else {
        256
    }
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

private fun add(p: Array<LongArray>, q: Array<LongArray>) {
    val a = gf()
    val b = gf()
    val c = gf()
    val d = gf()
    val e = gf()
    val f = gf()
    val g = gf()
    val h = gf()
    val t = gf()

    Z(a, p[1], p[0])
    Z(t, q[1], q[0])
    M(a, a, t)
    A(b, p[0], p[1])
    A(t, q[0], q[1])
    M(b, b, t)
    M(c, p[3], q[3])
    M(c, c, D2)
    M(d, p[2], q[2])
    A(d, d, d)
    Z(e, b, a)
    Z(f, d, c)
    A(g, d, c)
    A(h, b, a)

    M(p[0], e, f)
    M(p[1], h, g)
    M(p[2], g, f)
    M(p[3], e, h)
}

private fun cswap(p: Array<LongArray>, q: Array<LongArray>, b: Int) {
    sel25519(p[0], q[0], b)
    sel25519(p[1], q[1], b)
    sel25519(p[2], q[2], b)
    sel25519(p[3], q[3], b)
}

private fun pack(r: IntArray, p: Array<LongArray>) {
    val tx = gf()
    val ty = gf()
    val zi = gf()

    inv25519(zi, p[2])

    M(tx, p[0], zi)
    M(ty, p[1], zi)

    pack25519(r, ty)

    r[31] = r[31] xor (par25519(tx) shl 7)
}

private fun scalarmult(p: Array<LongArray>, q: Array<LongArray>, s: IntArray) {
    var b: Int

    set25519(p[0], gf0)
    set25519(p[1], gf1)
    set25519(p[2], gf1)
    set25519(p[3], gf0)

    for (i in 255 downTo 0) {
        b = (s[(i / 8) or 0] shr (i and 7)) and 1
        cswap(p, q, b)
        add(q, p)
        add(p, p)
        cswap(p, q, b)
    }
}

private fun scalarbase(p: Array<LongArray>, s: IntArray) {
    val q = arrayOf(gf(), gf(), gf(), gf())
    set25519(q[0], X)
    set25519(q[1], Y)
    set25519(q[2], gf1)
    M(q[3], X, Y)
    scalarmult(p, q, s)
}

private val L = intArrayOf(
    0xed, 0xd3, 0xf5, 0x5c, 0x1a, 0x63, 0x12, 0x58,
    0xd6, 0x9c, 0xf7, 0xa2, 0xde, 0xf9, 0xde, 0x14,
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0x10)

private fun modL(r: IntArray, x: IntArray) {
    var carry: Int

    for (i in 63 downTo 32) {
        carry = 0
        var j = i - 32
        val k = i - 12
        while (j < k) {
            x[j] += carry - 16 * x[i] * L[j - (i - 32)]
            carry = (x[j] + 128) shr 8
            x[j] -= carry * 256
            ++j
        }
        x[j] += carry
        x[i] = 0
    }

    carry = 0
    for (j in 0..31) {
        x[j] += carry - (x[31] shr 4) * L[j]
        carry = x[j] shr 8
        x[j] = x[j] and 255
    }

    for (j in 0..31) {
        x[j] -= carry * L[j]
    }

    for (i in 0..31) {
        x[i + 1] += x[i] shr 8
        r[i] = x[i] and 255
    }
}

private fun reduce(r: IntArray) {
    val x = IntArray(64)
    r.copyInto(x, endIndex = 64)
    r.fill(0, toIndex = 64)
    modL(r, x)
}

// Like crypto_sign, but uses secret key directly in hash.
private fun crypto_sign_direct(sm: IntArray, m: IntArray, n: Int, sk: IntArray): Int {
    val h = IntArray(64)
    val r = IntArray(64)
    val x = IntArray(64)
    val p = arrayOf(gf(), gf(), gf(), gf())

    for (i in 0 until n) {
        sm[64 + i] = m[i]
    }

    for (i in 0..31) {
        sm[32 + i] = sk[i]
    }

    crypto_hash(r, sm.copyOfRange(32, sm.size), n + 32)
    reduce(r)
    scalarbase(p, r)
    pack(sm, p)

    for (i in 0..31) {
        sm[i + 32] = sk[32 + i]
    }

    crypto_hash(h, sm, n + 64)
    reduce(h)

    x.fill(0, toIndex = 64)
    r.copyInto(x, endIndex = 32)

    for (i in 0..31) {
        for (j in 0..31) {
            x[i + j] += h[i] * sk[j]
        }
    }

    val tmp = sm.copyOfRange(32, sm.size)
    modL(tmp, x)
    for (i in tmp.indices) {
        sm[32 + i] = tmp[i]
    }

    return n + 64
}

// Note: sm must be n+128.
private fun crypto_sign_direct_rnd(sm: IntArray, m: IntArray, n: Int, sk: IntArray, rnd: IntArray): Int {
    val h = IntArray(64)
    val r = IntArray(64)
    val x = IntArray(64)
    val p = arrayOf(gf(), gf(), gf(), gf())

    // Hash separation.
    sm[0] = 0xfe
    sm.fill(0xff, fromIndex = 1, toIndex = 32)

    // Secret key.
    for (i in 0..31) {
        sm[32 + i] = sk[i]
    }

    // Message.
    for (i in 0 until n) {
        sm[64 + i] = m[i]
    }

    // Random suffix.
    for (i in 0..63) {
        sm[n + 64 + i] = rnd[i]
    }

    crypto_hash(r, sm, n + 128)
    reduce(r)
    scalarbase(p, r)
    pack(sm, p)

    for (i in 0..31) {
        sm[i + 32] = sk[32 + i]
    }

    crypto_hash(h, sm, n + 64)
    reduce(h)

    // Wipe out random suffix.
    for (i in 0..63) {
        sm[n + 64 + i] = 0
    }

    x.fill(0, toIndex = 64)
    r.copyInto(x, endIndex = 32)

    for (i in 0..31) {
        for (j in 0..31) {
            x[i + j] += h[i] * sk[j]
        }
    }

    val tmp = sm.copyOfRange(32, n + 64)
    modL(tmp, x)
    for (i in tmp.indices) {
        sm[32 + i] = tmp[i]
    }

    return n + 64
}

private fun curve25519_sign(sm: IntArray, m: IntArray, n: Int, sk: IntArray, opt_rnd: IntArray?): Int {
    // If opt_rnd is provided, sm must have n + 128,
    // otherwise it must have n + 64 bytes.

    // Convert Curve25519 secret key into Ed25519 secret key (includes pub key).
    val edsk = IntArray(64)
    val p = arrayOf(gf(), gf(), gf(), gf())

    sk.copyInto(edsk, endIndex = 32)

    // Ensure private key is in the correct format.
    edsk[0] = edsk[0] and 248
    edsk[31] = edsk[31] and 127
    edsk[31] = edsk[31] or 64

    scalarbase(p, edsk)

    val tmp = edsk.copyOfRange(32, edsk.size)
    pack(tmp, p)
    for (i in tmp.indices) {
        edsk[32 + i] = tmp[i]
    }

    // Remember sign bit.
    val signBit = edsk[63] and 128

    val smlen = if (opt_rnd != null) {
        crypto_sign_direct_rnd(sm, m, n, edsk, opt_rnd)
    } else {
        crypto_sign_direct(sm, m, n, edsk)
    }

    // Copy sign bit from public key into signature.
    sm[63] = sm[63] or signBit
    return smlen
}

private fun unpackneg(r: Array<LongArray>, p: IntArray): Int {
    val t = gf()
    val chk = gf()
    val num = gf()
    val den = gf()
    val den2 = gf()
    val den4 = gf()
    val den6 = gf()

    set25519(r[2], gf1)
    unpack25519(r[1], p)

    S(num, r[1])
    M(den, num, D)
    Z(num, num, r[2])
    A(den, r[2], den)

    S(den2, den)
    S(den4, den2)
    M(den6, den4, den2)
    M(t, den6, num)
    M(t, t, den)

    pow2523(t, t)
    M(t, t, num)
    M(t, t, den)
    M(t, t, den)
    M(r[0], t, den)

    S(chk, r[0])
    M(chk, chk, den)

    if (neq25519(chk, num) != 0) {
        M(r[0], r[0], I)
    }

    S(chk, r[0])
    M(chk, chk, den)

    if (neq25519(chk, num) != 0) {
        return -1
    }

    if (par25519(r[0]) == (p[31] shr 7)) {
        Z(r[0], gf0, r[0])
    }

    M(r[3], r[0], r[1])

    return 0
}

private fun crypto_sign_open(m: IntArray, sm: IntArray, n: Int, pk: IntArray): Int {
    if (n < 64) {
        return -1
    }
    val q = arrayOf(gf(), gf(), gf(), gf())
    if (unpackneg(q, pk) != 0) {
        return -1
    }

    val t = IntArray(32)
    val h = IntArray(64)
    val p = arrayOf(gf(), gf(), gf(), gf())

    sm.copyInto(m, endIndex = n)

    for (i in 0..31) {
        m[i + 32] = pk[i]
    }

    crypto_hash(h, m, n)
    reduce(h)
    scalarmult(p, q, h)

    scalarbase(q, sm.copyOfRange(32, sm.size))
    add(p, q)
    pack(t, p)

    val nn = n - 64
    if (crypto_verify(sm, 0, t, 0) != 0) {
        m.fill(0, toIndex = nn)
        return -1
    }

    for (i in 0 until nn) {
        m[i] = sm[i + 64]
    }

    return nn
}

// Converts Curve25519 public key back to Ed25519 public key.
// edwardsY = (montgomeryX - 1) / (montgomeryX + 1)
private fun convertPublicKey(pk: IntArray): IntArray {
    val z = IntArray(32)
    val x = gf()
    val a = gf()
    val b = gf()

    unpack25519(x, pk)

    A(a, x, gf1)
    Z(b, x, gf1)
    inv25519(a, a)
    M(a, a, b)

    pack25519(z, a)
    return z
}

private fun curve25519_sign_open(m: IntArray, sm: IntArray, n: Int, pk: IntArray): Int {
    // Convert Curve25519 public key into Ed25519 public key.
    val edpk = convertPublicKey(pk)

    // Restore sign bit from signature.
    edpk[31] = edpk[31] or (sm[63] and 128)

    // Remove sign bit from signature.
    sm[63] = sm[63] and 127

    // Verify signed message.
    return crypto_sign_open(m, sm, n, edpk)
}
