package ton.crypto

import kotlin.experimental.xor
import kotlin.random.Random
import kotlin.test.*
import kotlin.test.Test

class Test {

    @Test
    fun generateKeys() {
        val keys = Crypto.publicKey()
        assertEquals(keys.privateKey.size, 32)
        assertEquals(keys.publicKey.size, 32)
    }

    @Test
    fun signAndVerify() {
        val keys = Crypto.publicKey()
        val msg = byteArrayOf(1, 2, 3, 4, 5)
        val sig = Crypto.sign(keys.privateKey, msg, null)

        println("secret: " + hex(keys.privateKey))
        println("public: " + hex(keys.publicKey))
        println("signat: " + hex(sig))

        assertEquals(64, sig.size)
        assertTrue(Crypto.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun deterministicSignaturesIfNotRandomized() {
        val keys = Crypto.publicKey()
        val msg = byteArrayOf(1, 2, 3, 4, 5)
        val sig1 = Crypto.sign(keys.privateKey, msg, null)
        val sig2 = Crypto.sign(keys.privateKey, msg, null)
        assertEquals(hex(sig1), hex(sig2))
    }

    @Test
    fun differentSignaturesIfRandomized() {
        val keys = Crypto.publicKey()
        val msg = byteArrayOf(1, 2, 3, 4, 5)
        val sig0 = Crypto.sign(keys.privateKey, msg, null) // not randomized
        val sig1 = Crypto.sign(keys.privateKey, msg, Random.nextBytes(64))
        val sig2 = Crypto.sign(keys.privateKey, msg, Random.nextBytes(64))

        assertNotEquals(hex(sig1), hex(sig2))
        assertNotEquals(hex(sig0), hex(sig1))
        assertNotEquals(hex(sig0), hex(sig2))
    }

    @Test
    fun signRandomizedAndVerify() {
        val random = Random.nextBytes(64)
        val keys = Crypto.publicKey()
        val msg = byteArrayOf(1, 2, 3, 4, 5)
        val sig = Crypto.sign(keys.privateKey, msg, random)

        assertEquals(sig.size, 64)
        assertTrue(Crypto.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun notVerifyBadSignature() {
        val keys = Crypto.publicKey()
        val msg = byteArrayOf(1, 2, 3, 4, 5)
        val sig = Crypto.sign(keys.privateKey, msg, null)

        assertTrue(Crypto.verify(keys.publicKey, msg, sig))

        sig[0] = sig[0] xor sig[0]
        sig[1] = sig[1] xor sig[1]
        sig[2] = sig[2] xor sig[2]
        sig[3] = sig[3] xor sig[3]

        assertFalse(Crypto.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun notVerifyBadMessage() {
        val keys = Crypto.publicKey()
        var msg = byteArrayOf(1, 2, 3, 4, 5)
        val sig = Crypto.sign(keys.privateKey, msg, null)

        assertTrue(Crypto.verify(keys.publicKey, msg, sig))

        msg = byteArrayOf(1, 2, 3, 4)

        assertFalse(Crypto.verify(keys.publicKey, msg, sig))
    }

    @Test
    fun signMessageAndOpenMessage() {
        val keys = Crypto.publicKey()
        val msg = "the essential is invisible to the eyes"
        val signedMsg = Crypto.signMessage(keys.privateKey, msg.encodeToByteArray(), null)

        assertEquals(msg.length + 64, signedMsg.size) // *** R
        assertEquals(msg, Crypto.openMessageStr(keys.publicKey, signedMsg))
    }

    @Test
    fun calculateKeyAgreement() {
        val seed1 = Random.nextBytes(32)
        val seed2 = Random.nextBytes(32)

        val k1 = Crypto.publicKey(seed1)
        val k2 = Crypto.publicKey(seed2)

        val sk1 = Crypto.sharedKey(k2.privateKey, k1.publicKey)
        val sk2 = Crypto.sharedKey(k1.privateKey, k2.publicKey)

        assertEquals(sk1.contentToString(), sk2.contentToString())
    }

    @Test
    fun sha512() {
        val message = "test"
        val hash = sha512(message.encodeToByteArray())

        assertEquals("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff",
            hex(hash))
    }
}
