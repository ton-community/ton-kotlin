package org.ton.mnemonic

import io.ktor.utils.io.core.*
import org.ton.crypto.Ed25519
import org.ton.crypto.SecureRandom
import org.ton.crypto.digest.Digest
import org.ton.crypto.kdf.PKCSS2ParametersGenerator
import org.ton.crypto.mac.hmac.HMac
import kotlin.jvm.JvmStatic
import kotlin.random.Random

public interface Mnemonic {
    public companion object {
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

        public fun mnemonicWords(): Array<String> = MNEMONIC_WORD_LIST

        @JvmStatic
        public suspend fun generate(
            password: String = "",
            wordCount: Int = DEFAULT_WORD_COUNT,
            wordlist: Array<String> = mnemonicWords(),
            random: Random = SecureRandom
        ): Array<String> {
            while (true) {
                val mnemonic = Array(wordCount) {
                    wordlist[random.nextInt(wordlist.size)] // nextInt() takes exclusive upper limit, we're safe here
                }

                if (password.isNotEmpty() && !isPasswordNeeded(mnemonic)) {
                    continue
                }
                if (!isBasicSeed(toEntropy(mnemonic, password))) {
                    continue
                }

                return mnemonic
            }
        }

        @JvmStatic
        public fun isPasswordNeeded(mnemonic: Array<String>): Boolean {
            val entropy = toEntropy(mnemonic)
            return isPasswordSeed(entropy) && !isBasicSeed(entropy)
        }

        @JvmStatic
        public suspend fun isValid(
            mnemonic: Array<String>,
            password: String = "",
            wordlist: Array<String> = mnemonicWords()
        ): Boolean {
            if (!mnemonic.all { wordlist.contains(it) }) {
                return false
            }

            if (password.isNotEmpty() && !isPasswordNeeded(mnemonic)) {
                return false
            }

            return isBasicSeed(toEntropy(mnemonic, password))
        }

        // Returns a pair of public and secret keys from the given mnemonic
        @JvmStatic
        public fun toKeyPair(mnemonic: Array<String>, password: String = ""): Pair<ByteArray, ByteArray> {
            val sk = toSeed(mnemonic, password)
            return Pair(Ed25519.publicKey(sk), sk)
        }

        @JvmStatic
        public fun toSeed(mnemonic: Array<String>, password: String = ""): ByteArray {
            val pbdkf2Sha512 = PKCSS2ParametersGenerator(
                digest = Digest("SHA-512"),
                password = toEntropy(mnemonic, password),
                salt = DEFAULT_SALT.toByteArray(),
                iterationCount = DEFAULT_ITERATIONS
            )
            return pbdkf2Sha512.generateDerivedParameters(512).sliceArray(0..31)
        }

        @JvmStatic
        public fun toEntropy(mnemonic: Array<String>, password: String = ""): ByteArray {
            val hMac = HMac(Digest("SHA-512"), mnemonic.joinToString(" ").toByteArray())
            hMac += password.toByteArray()
            return hMac.build()
        }

        @JvmStatic
        public fun isBasicSeed(entropy: ByteArray): Boolean {
            val pbdkf2Sha512 = PKCSS2ParametersGenerator(
                digest = Digest("SHA-512"),
                password = entropy,
                salt = DEFAULT_BASIC_SALT.toByteArray(),
                iterationCount = DEFAULT_BASIC_ITERATIONS
            )
            return pbdkf2Sha512.generateDerivedParameters(512).first() == 0.toByte()
        }

        @JvmStatic
        public fun isPasswordSeed(entropy: ByteArray): Boolean {
            val pbdkf2Sha512 = PKCSS2ParametersGenerator(
                digest = Digest("SHA-512"),
                password = entropy,
                salt = DEFAULT_PASSWORD_SALT.toByteArray(),
                iterationCount = DEFAULT_PASSWORD_ITERATIONS
            )
            return pbdkf2Sha512.generateDerivedParameters(512).first() == 1.toByte()
        }
    }
}
