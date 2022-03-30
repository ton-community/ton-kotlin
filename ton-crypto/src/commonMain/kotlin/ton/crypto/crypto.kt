@file:Suppress("NOTHING_TO_INLINE")

package ton.crypto

import kotlin.math.floor
import kotlin.random.Random

object Crypto {
    fun sharedKey(secretKey: ByteArray, publicKey: ByteArray): ByteArray {
        val sharedKey = IntArray(32)
        crypto_scalarmult(sharedKey, secretKey.toIntArray(), publicKey.toIntArray())
        return sharedKey.toByteArray()
    }

    fun signMessage(secretKey: ByteArray, msg: ByteArray, nonce: ByteArray? = null): ByteArray {
        return if (nonce != null) {
            val buf = IntArray(128 + msg.size)
            curve25519_sign(buf, msg.toIntArray(), msg.size, secretKey.toIntArray(), nonce.toIntArray())
            buf.copyOfRange(0, 64 + msg.size)
        } else {
            val signedMsg = IntArray(64 + msg.size)
            curve25519_sign(signedMsg, msg.toIntArray(), msg.size, secretKey.toIntArray(), null)
            signedMsg
        }.toByteArray()
    }

    fun openMessageStr(publicKey: ByteArray, signedMsg: ByteArray): String? {
        val m = openMessage(publicKey, signedMsg) ?: return null
        return buildString {
            for (element in m) {
                append(element.toInt().toChar())
            }
        }
    }

    fun openMessage(publicKey: ByteArray, signedMsg: ByteArray): ByteArray? {
        val tmp = IntArray(signedMsg.size)
        val mlen = curve25519_sign_open(tmp, signedMsg.toIntArray(), signedMsg.size, publicKey.toIntArray())
        if (mlen < 0) {
            return null
        }
        val m = IntArray(mlen)
        for (i in m.indices) {
            m[i] = tmp[i]
        }
        return m.toByteArray()
    }

    fun sign(secretKey: ByteArray, msg: ByteArray, opt_random: ByteArray?): ByteArray {
        var len = 64
        if (opt_random != null) {
            len = 128
        }
        val buf = IntArray(len + msg.size)
        curve25519_sign(buf, msg.toIntArray(), msg.size, secretKey.toIntArray(), opt_random?.toIntArray())

        val signature = IntArray(64)
        buf.copyInto(signature, endIndex = signature.size)
        return signature.toByteArray()
    }

    fun verify(publicKey: ByteArray, msg: ByteArray, signature: ByteArray): Boolean {
        val sm = IntArray(64 + msg.size)
        val m = IntArray(64 + msg.size)

        signature.toIntArray().copyInto(sm, endIndex = 64)

        for (i in msg.indices) {
            sm[i + 64] = msg[i].toInt()
        }

        return curve25519_sign_open(m, sm, sm.size, publicKey.toIntArray()) >= 0
    }

    fun generateKeyPair(random: Random = Random.Default) = generateKeyPair(random.nextBytes(32))
    fun generateKeyPair(seed: ByteArray): KeyPair {
        val sk = IntArray(32)
        val pk = IntArray(32)

        seed.toIntArray().copyInto(sk, endIndex = 32)

        crypto_scalarmult(pk, sk)

        // Turn secret key into the correct format.
        sk[0] = sk[0] and 248
        sk[31] = sk[31] and 127
        sk[31] = sk[31] or 64

        // Remove sign bit from public key.
        pk[31] = pk[31] and 127

        return KeyPair(sk.toByteArray(), pk.toByteArray())
    }

    data class KeyPair(val privateKey: ByteArray, val publicKey: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as KeyPair

            if (!privateKey.contentEquals(other.privateKey)) return false
            if (!publicKey.contentEquals(other.publicKey)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = privateKey.contentHashCode()
            result = 31 * result + publicKey.contentHashCode()
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

internal fun A(o: LongArray, a: LongArray, b: LongArray) {
    for (i in 0..15) {
        o[i] = a[i] + b[i]
    }
}

internal fun Z(o: LongArray, a: LongArray, b: LongArray) {
    for (i in 0..15) {
        o[i] = a[i] - b[i]
    }
}

internal fun M(o: LongArray, a: LongArray, b: LongArray) {
    val t = LongArray(32)

    for (i in 0..15) {
        for (j in 0..15) {
            t[j + i] += a[i] * b[j]
        }
    }

    t[0] += 38 * t[16]
    t[1] += 38 * t[17]
    t[2] += 38 * t[18]
    t[3] += 38 * t[19]
    t[4] += 38 * t[20]
    t[5] += 38 * t[21]
    t[6] += 38 * t[22]
    t[7] += 38 * t[23]
    t[8] += 38 * t[24]
    t[9] += 38 * t[25]
    t[10] += 38 * t[26]
    t[11] += 38 * t[27]
    t[12] += 38 * t[28]
    t[13] += 38 * t[29]
    t[14] += 38 * t[30]
    // t15 left as is

    // first car
    var c: Long = 1
    var v: Long

    v = t[0] + c + 65535; c = floor(v / 65536.0).toLong(); t[0] = v - c * 65536
    v = t[1] + c + 65535; c = floor(v / 65536.0).toLong(); t[1] = v - c * 65536
    v = t[2] + c + 65535; c = floor(v / 65536.0).toLong(); t[2] = v - c * 65536
    v = t[3] + c + 65535; c = floor(v / 65536.0).toLong(); t[3] = v - c * 65536
    v = t[4] + c + 65535; c = floor(v / 65536.0).toLong(); t[4] = v - c * 65536
    v = t[5] + c + 65535; c = floor(v / 65536.0).toLong(); t[5] = v - c * 65536
    v = t[6] + c + 65535; c = floor(v / 65536.0).toLong(); t[6] = v - c * 65536
    v = t[7] + c + 65535; c = floor(v / 65536.0).toLong(); t[7] = v - c * 65536
    v = t[8] + c + 65535; c = floor(v / 65536.0).toLong(); t[8] = v - c * 65536
    v = t[9] + c + 65535; c = floor(v / 65536.0).toLong(); t[9] = v - c * 65536
    v = t[10] + c + 65535; c = floor(v / 65536.0).toLong(); t[10] = v - c * 65536
    v = t[11] + c + 65535; c = floor(v / 65536.0).toLong(); t[11] = v - c * 65536
    v = t[12] + c + 65535; c = floor(v / 65536.0).toLong(); t[12] = v - c * 65536
    v = t[13] + c + 65535; c = floor(v / 65536.0).toLong(); t[13] = v - c * 65536
    v = t[14] + c + 65535; c = floor(v / 65536.0).toLong(); t[14] = v - c * 65536
    v = t[15] + c + 65535; c = floor(v / 65536.0).toLong(); t[15] = v - c * 65536
    t[0] += c - 1 + 37 * (c - 1)

    // second car
    c = 1
    v = t[0] + c + 65535; c = floor(v / 65536.0).toLong(); t[0] = v - c * 65536
    v = t[1] + c + 65535; c = floor(v / 65536.0).toLong(); t[1] = v - c * 65536
    v = t[2] + c + 65535; c = floor(v / 65536.0).toLong(); t[2] = v - c * 65536
    v = t[3] + c + 65535; c = floor(v / 65536.0).toLong(); t[3] = v - c * 65536
    v = t[4] + c + 65535; c = floor(v / 65536.0).toLong(); t[4] = v - c * 65536
    v = t[5] + c + 65535; c = floor(v / 65536.0).toLong(); t[5] = v - c * 65536
    v = t[6] + c + 65535; c = floor(v / 65536.0).toLong(); t[6] = v - c * 65536
    v = t[7] + c + 65535; c = floor(v / 65536.0).toLong(); t[7] = v - c * 65536
    v = t[8] + c + 65535; c = floor(v / 65536.0).toLong(); t[8] = v - c * 65536
    v = t[9] + c + 65535; c = floor(v / 65536.0).toLong(); t[9] = v - c * 65536
    v = t[10] + c + 65535; c = floor(v / 65536.0).toLong(); t[10] = v - c * 65536
    v = t[11] + c + 65535; c = floor(v / 65536.0).toLong(); t[11] = v - c * 65536
    v = t[12] + c + 65535; c = floor(v / 65536.0).toLong(); t[12] = v - c * 65536
    v = t[13] + c + 65535; c = floor(v / 65536.0).toLong(); t[13] = v - c * 65536
    v = t[14] + c + 65535; c = floor(v / 65536.0).toLong(); t[14] = v - c * 65536
    v = t[15] + c + 65535; c = floor(v / 65536.0).toLong(); t[15] = v - c * 65536
    t[0] += c - 1 + 37 * (c - 1)

    t.copyInto(o, endIndex = 16)
}

internal inline fun S(o: LongArray, a: LongArray) = M(o, a, a)

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

    n.copyInto(z, endIndex = 31)
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

internal fun add(p: Array<LongArray>, q: Array<LongArray>) {
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


internal fun pack(r: IntArray, p: Array<LongArray>) {
    val tx = gf()
    val ty = gf()
    val zi = gf()

    inv25519(zi, p[2])

    M(tx, p[0], zi)
    M(ty, p[1], zi)

    pack25519(r, ty)

    r[31] = r[31] xor (par25519(tx) shl 7)
}

internal fun scalarmult(p: Array<LongArray>, q: Array<LongArray>, s: IntArray) {
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

internal inline fun cswap(p: Array<LongArray>, q: Array<LongArray>, b: Int) {
    sel25519(p[0], q[0], b)
    sel25519(p[1], q[1], b)
    sel25519(p[2], q[2], b)
    sel25519(p[3], q[3], b)
}

internal fun scalarbase(p: Array<LongArray>, s: IntArray) {
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

internal fun reduce(r: IntArray) {
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

    sha512(r, sm.copyOfRange(32, sm.size), n + 32)
    reduce(r)
    scalarbase(p, r)
    pack(sm, p)

    for (i in 0..31) {
        sm[i + 32] = sk[32 + i]
    }

    sha512(h, sm, n + 64)
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

    sha512(r, sm, n + 128)
    reduce(r)
    scalarbase(p, r)
    pack(sm, p)

    for (i in 0..31) {
        sm[i + 32] = sk[32 + i]
    }

    sha512(h, sm, n + 64)
    reduce(h)

    // Wipe out random suffix.
    sm.fill(0, fromIndex = n + 64, toIndex = n + 128)
//    for (i in 0..63) {
//        sm[n + 64 + i] = 0
//    }

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

internal fun unpackneg(r: Array<LongArray>, p: IntArray): Int {
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


