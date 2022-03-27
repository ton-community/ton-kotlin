package curve25519

import kotlin.test.*
import kotlin.test.Test

class Test {
    @Test
    fun generateKeys() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        assertEquals(keys.privateKey.size, 32)
        assertEquals(keys.publicKey.size, 32)
    }

    @Test
    fun signAndVerify() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        val msg = intArrayOf(1, 2, 3, 4, 5)
        val sig = Curve25519.sign(keys.privateKey, msg, null)

        println("secret: " + keys.privateKey.toHex())
        println("public: " + keys.publicKey.toHex())
        println("signat: " + sig.toHex())

        assertEquals(sig.size, 64)
        assertTrue(Curve25519.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun deterministicSignaturesIfNotRandomized() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        val msg = intArrayOf(1, 2, 3, 4, 5)
        val sig1 = Curve25519.sign(keys.privateKey, msg, null)
        val sig2 = Curve25519.sign(keys.privateKey, msg, null)
        assertEquals(sig1.toHex(), sig2.toHex())
    }

    @Test
    fun differentSignaturesIfRandomized() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        val msg = intArrayOf(1, 2, 3, 4, 5)
        val sig0 = Curve25519.sign(keys.privateKey, msg, null) // not randomized
        val sig1 = Curve25519.sign(keys.privateKey, msg, Curve25519.randomBytes(64))
        val sig2 = Curve25519.sign(keys.privateKey, msg, Curve25519.randomBytes(64))

        assertNotEquals(sig1.toHex(), sig2.toHex())
        assertNotEquals(sig0.toHex(), sig1.toHex())
        assertNotEquals(sig0.toHex(), sig2.toHex())
    }

    @Test
    fun signRandomizedAndVerify() {
        val seed = Curve25519.randomBytes(32)
        val random = Curve25519.randomBytes(64)
        val keys = Curve25519.generateKeyPair(seed)
        val msg = intArrayOf(1, 2, 3, 4, 5)
        val sig = Curve25519.sign(keys.privateKey, msg, random)

        assertEquals(sig.size, 64)
        assertTrue(Curve25519.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun notVerifyBadSignature() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        val msg = intArrayOf(1, 2, 3, 4, 5)
        val sig = Curve25519.sign(keys.privateKey, msg, null)

        assertTrue(Curve25519.verify(keys.publicKey, msg, sig))

        sig[0] = sig[0] xor sig[0]
        sig[1] = sig[1] xor sig[1]
        sig[2] = sig[2] xor sig[2]
        sig[3] = sig[3] xor sig[3]

        assertFalse(Curve25519.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun notVerifyBadMessage() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        var msg = intArrayOf(1, 2, 3, 4, 5)
        val sig = Curve25519.sign(keys.privateKey, msg, null)

        assertTrue(Curve25519.verify(keys.publicKey, msg, sig))

        msg = intArrayOf(1, 2, 3, 4)

        assertFalse(Curve25519.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun signMessageAndOpenMessage() {
        val seed = Curve25519.randomBytes(32)
        val keys = Curve25519.generateKeyPair(seed)
        val msg = "the essential is invisible to the eyes"
        val signedMsg = Curve25519.signMessage(keys.privateKey, msg.toIntArray(), null)

        assertEquals(signedMsg.size, msg.length + 64) // *** R
        assertEquals(Curve25519.openMessageStr(keys.publicKey, signedMsg), msg)
    }

    @Test
    fun calculateKeyAgreement() {
        val seed1 = Curve25519.randomBytes(32)
        val seed2 = Curve25519.randomBytes(32)

        val k1 = Curve25519.generateKeyPair(seed1)
        val k2 = Curve25519.generateKeyPair(seed2)

        val sk1 = Curve25519.sharedKey(k2.privateKey, k1.publicKey)
        val sk2 = Curve25519.sharedKey(k1.privateKey, k2.publicKey)

        assertEquals(sk1.toHex(), sk2.toHex())
    }
}

private val CHARS = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun IntArray.toHex(): String = buildString {
    for (i in 0 until this@toHex.size) {
        val v = this@toHex[i]
        val char2 = CHARS[v and 0x0f]
        val char1 = CHARS[v shr 4 and 0x0f]
        append("$char1$char2")
    }
}

fun String.toIntArray(): IntArray {
    val ca = this.toCharArray()
    val re = IntArray(ca.size)
    for (i in ca.indices) {
        re[i] = ca[i].code
    }
    return re
}