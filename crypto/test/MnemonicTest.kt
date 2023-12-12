package org.ton.mnemonic

import kotlinx.coroutines.runBlocking
import org.ton.crypto.DecryptorAes
import org.ton.crypto.EncryptorAes
import org.ton.crypto.digest.Digest
import org.ton.crypto.hex
import org.ton.crypto.mac.hmac.HMac
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MnemonicTest {
    @Test
    fun `mnemonic generation`() = runBlocking {
        val mnemonic0 = Mnemonic.generate()
        assertTrue(Mnemonic.isValid(mnemonic0))
        assertTrue(Mnemonic.isBasicSeed(Mnemonic.toEntropy(mnemonic0)))
        // sometimes it's a password seed also
//        assertFalse(Mnemonic.isPasswordSeed(Mnemonic.toEntropy(mnemonic0)))
        assertFalse(Mnemonic.isPasswordNeeded(mnemonic0))

        val mnemonic1 = Mnemonic.generate(password = "password")
        assertFalse(Mnemonic.isValid(mnemonic1))
        assertTrue(Mnemonic.isValid(mnemonic1, "password"))
        // sometimes it's a basic seed also
//        assertFalse(Mnemonic.isBasicSeed(Mnemonic.toEntropy(mnemonic1)))
        assertTrue(Mnemonic.isPasswordSeed(Mnemonic.toEntropy(mnemonic1)))
        assertTrue(Mnemonic.isPasswordNeeded(mnemonic1))
    }

    @Test
    fun `mnemonic validation`() = runBlocking {
        assertTrue(Mnemonic.isValid(listOf("kangaroo", "hen", "toddler", "resist")))
        assertTrue(Mnemonic.isValid(listOf("disease", "adult", "device", "grit")))
        assertFalse(Mnemonic.isValid(listOf("disease", "adult", "device", "grit"), "password"))
        assertFalse(
            Mnemonic.isValid(
                listOf("disease", "adult", "device", "grit"),
                wordlist = listOf("bean", "bean", "bean")
            )
        )
        assertTrue(Mnemonic.isValid(listOf("deal", "wrap", "runway", "possible"), "password"))
        assertFalse(Mnemonic.isValid(listOf("deal", "wrap", "runway", "possible"), "notthepassword"))
        assertFalse(Mnemonic.isValid(listOf("deal", "wrap", "runway", "possible")))
    }

    @Test
    fun `mnemonic to seed`() = runBlocking {
        assertEquals(
            "a356fc9b35cb9b463adf65b2414bbebcec1d0d0d99fc4fc14e259395c128022d",
            hex(Mnemonic.toSeed(listOf("kangaroo", "hen", "toddler", "resist")))
        )
        assertEquals(
            "fb1df381306619a2128295e73e05c6013211f589e8bebd602469cdf1fc04a1cb",
            hex(Mnemonic.toSeed(listOf("disease", "adult", "device", "grit")))
        )
        assertEquals(
            "3078a0d183d0f0e88c4f8a5979590612f230a3228912838b66bcc9e9053b2584",
            hex(Mnemonic.toSeed(listOf("deal", "wrap", "runway", "possible"), "password"))
        )
    }

    @Test
    fun testa() = runBlocking {
        val myKey = HMac(Digest.sha512(), "test12121212".encodeToByteArray()).build()

        val encrypted = EncryptorAes(myKey).encrypt("data".encodeToByteArray())
        println("encrypted: ${encrypted.decodeToString()}")
        val decrypted = DecryptorAes(myKey).decrypt(encrypted)
        println("decrypted: ${decrypted.decodeToString()}")
    }
}
