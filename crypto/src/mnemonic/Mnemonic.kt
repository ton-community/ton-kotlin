package org.ton.mnemonic

import io.github.andreypfau.curve25519.ed25519.Ed25519
import io.github.andreypfau.curve25519.ed25519.Ed25519PrivateKey
import io.github.andreypfau.kotlinx.crypto.hmac.HMac
import io.github.andreypfau.kotlinx.crypto.pbkdf2.Pbkdf2
import io.github.andreypfau.kotlinx.crypto.sha2.SHA512
import org.ton.crypto.SecureRandom
import org.ton.crypto.mnemonic.BIP39_ENGLISH
import kotlin.jvm.JvmStatic
import kotlin.random.Random

public class Mnemonic private constructor(
    private val wordsProvider: () -> Collection<String>,
    private val password: ByteArray,
) {
    public constructor(words: String, password: ByteArray) : this({ normalizeAndSplit(words) }, password)
    public constructor(words: Collection<String>, password: ByteArray) : this({ words }, password)
    public constructor(words: String) : this(words, EMPTY_BYTES)
    public constructor(words: Collection<String>) : this({ words }, EMPTY_BYTES)

    public val words: Collection<String> = wordsProvider()

    public fun isBasicSeed(): Boolean {
        val pbkdf2 = Pbkdf2(
            digest = SHA512(),
            password = toEntropy(),
            salt = DEFAULT_BASIC_SALT.encodeToByteArray(),
            iterationCount = DEFAULT_BASIC_ITERATIONS
        )
        return pbkdf2.deriveKey()[0] == 0.toByte()
    }

    public fun isPasswordSeed(): Boolean {
        val pbkdf2 = Pbkdf2(
            digest = SHA512(),
            password = toEntropy(),
            salt = DEFAULT_PASSWORD_SALT.encodeToByteArray(),
            iterationCount = 1
        )
        return pbkdf2.deriveKey()[0] == 1.toByte()
    }

    public fun isValid(): Boolean {
        return if (password.isNotEmpty()) {
            isPasswordSeed()
        } else {
            isBasicSeed()
        }
    }

    public fun toPrivateKey(): Ed25519PrivateKey {
        val seed = ByteArray(Ed25519.SEED_SIZE_BYTES)
        toSeed(seed)
        return Ed25519.keyFromSeed(seed)
    }

    public fun toEntropy(destination: ByteArray, destinationOffset: Int = 0) {
        val words = wordsProvider().joinToString(" ").encodeToByteArray()
        val hMac = HMac(
            digest = SHA512(),
            key = words
        )
        hMac.update(password)
        hMac.digest(destination, destinationOffset)
    }

    public fun toEntropy(): ByteArray {
        val destination = ByteArray(64)
        toEntropy(destination)
        return destination
    }

    public fun toSeed(destination: ByteArray, destinationOffset: Int = 0) {
        val pbkdf2 = Pbkdf2(
            digest = SHA512(),
            password = toEntropy(),
            salt = DEFAULT_SALT.encodeToByteArray(),
            iterationCount = DEFAULT_ITERATIONS
        )
        pbkdf2.deriveKey(destination, destinationOffset, destination.size - destinationOffset)
    }

    public fun toSeed(): ByteArray {
        val destination = ByteArray(64)
        toSeed(destination)
        return destination
    }

    override fun toString(): String = buildString {
        append("Mnemonic(")
        append(wordsProvider())
        if (password.isNotEmpty()) {
            append(", password=${password.decodeToString()}")
        }
        append(")")
    }

    public companion object {
        private val EMPTY_BYTES = ByteArray(0)

        // Number of PBKDF2 iterations used to generate seed
        public const val DEFAULT_ITERATIONS: Int = 100000

        // Default salt for PBKDF2 used to generate seed
        public const val DEFAULT_SALT: String = "TON default seed"

        // Number of PBKDF2 iterations used to check, if mnemonic phrase is valid
        public const val DEFAULT_BASIC_ITERATIONS: Int = 390 // max(1, floor(DEFAULT_ITERATIONS / 256))

        // Default salt used to check mnemonic phrase validity
        public const val DEFAULT_BASIC_SALT: String = "TON seed version"

        // Number of PBKDF2 iterations used to check, if mnemonic phrase requires a password
        public const val DEFAULT_PASSWORD_ITERATIONS: Int = 1

        // Default salt used to check, if mnemonic phrase requires a password
        public const val DEFAULT_PASSWORD_SALT: String = "TON fast seed version"

        public const val DEFAULT_WORD_COUNT: Int = 24

        @JvmStatic
        public fun bip39English(): List<String> = BIP39_ENGLISH

        @JvmStatic
        public fun generate(): Mnemonic = generate(SecureRandom, null, DEFAULT_WORD_COUNT)

        @JvmStatic
        public fun generate(password: String? = null, wordsCount: Int = DEFAULT_WORD_COUNT): Mnemonic =
            generate(SecureRandom, password, wordsCount)

        @JvmStatic
        public fun generate(random: Random, password: String? = null, wordsCount: Int = DEFAULT_WORD_COUNT): Mnemonic {
            require(wordsCount in 8..48) {
                "Invalid words count: $wordsCount"
            }
            var maxIterations = 256 * 20
            val hasPassword = !password.isNullOrBlank()
            if (hasPassword) {
                maxIterations *= 256
            }
            val bipWords = bip39English()
            val passwordBytes = password?.encodeToByteArray() ?: EMPTY_BYTES
            for (iteration in 0 until maxIterations) {
                val words = bipWords.random(random, wordsCount)
                val mnemonic = Mnemonic(words, passwordBytes)
                if (mnemonic.isValid()) {
                    return mnemonic
                }
                continue
            }

            throw IllegalStateException("Failed to generate mnemonic")
        }

        private fun List<String>.random(random: Random, count: Int): List<String> {
            val words = ArrayList<String>(count)
            val rnd = random.nextBytes((count * 11 + 7) / 8)
            for (i in 0 until count) {
                var index = 0
                for (j in 0 until 11) {
                    val offset = i * 11 + j
                    if ((rnd[offset / 8].toInt() and (1 shl (offset and 7))) != 0) {
                        index = index or (1 shl j)
                    }
                }
                words.add(this[index])
            }
            return words
        }

        private fun normalizeAndSplit(words: String): List<String> {
            return words.map {
                if (it.isLetter()) {
                    it.lowercaseChar()
                } else {
                    " "
                }
            }.joinToString("").split(" ").filter {
                it.isNotEmpty()
            }
        }
    }
}
