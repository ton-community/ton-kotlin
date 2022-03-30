package ton.crypto

import kotlin.math.floor

internal fun curve25519_sign_open(m: IntArray, sm: IntArray, n: Int, pk: IntArray): Int {
    // Convert Curve25519 public key into Ed25519 public key.
    val edpk = convertPublicKey(pk)

    // Restore sign bit from signature.
    edpk[31] = edpk[31] or (sm[63] and 128)

    // Remove sign bit from signature.
    sm[63] = sm[63] and 127

    // Verify signed message.
    return crypto_sign_open(m, sm, n, edpk)
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

    sha512(h, m, n)
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
internal fun convertPublicKey(pk: IntArray): IntArray {
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

internal fun par25519(a: LongArray): Int {
    val d = IntArray(32)
    pack25519(d, a)
    return d[0] and 1
}

internal fun unpack25519(o: LongArray, n: IntArray) {
    for (i in 0..15) {
        val value: Int = n[2 * i] + (n[2 * i + 1] shl 8)
        o[i] = value.toLong()
    }
    o[15] = o[15] and 0x7fff
}

internal fun pack25519(o: IntArray, n: LongArray) {
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

internal fun neq25519(a: LongArray, b: LongArray): Int {
    val c = IntArray(32)
    val d = IntArray(32)
    pack25519(c, a)
    pack25519(d, b)
    return crypto_verify(c, 0, d, 0)
}

internal fun inv25519(o: LongArray, i: LongArray) {
    val c = i.copyOf(16)
    for (a in 253 downTo 0) {
        S(c, c)
        if (a != 2 && a != 4) {
            M(c, c, i)
        }
    }
    c.copyInto(o, endIndex = 16)
}

internal fun crypto_verify(x: IntArray, xi: Int, y: IntArray, yi: Int, n: Int = 32): Int {
    var d = 0
    for (i in 0 until n) {
        d = d or (x[xi + i] xor y[yi + i])
    }
    return (1 and ((d - 1) ushr 8)) - 1
}

internal fun set25519(r: LongArray, a: LongArray) {
    for (i in 0..15) {
        r[i] = a[i] or 0
    }
}

internal fun car25519(o: LongArray) {
    var v: Long
    var c = 1L
    for (i in 0..15) {
        v = o[i] + c + 65535
        c = floor(v / 65536.0).toLong()
        o[i] = v - c * 65536
    }
    o[0] += c - 1 + 37 * (c - 1)
}

internal fun sel25519(p: LongArray, q: LongArray, b: Int) {
    var t: Long
    val invb = (b - 1).inv()
    val c: Long = invb.toLong()
    for (i in 0..15) {
        t = c and (p[i] xor q[i])
        p[i] = p[i] xor t
        q[i] = q[i] xor t
    }
}
