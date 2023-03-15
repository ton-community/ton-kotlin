package org.ton.mnemonic

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import org.ton.crypto.SecureRandom
import org.ton.crypto.digest.Digest
import org.ton.crypto.kdf.PKCSS2ParametersGenerator
import org.ton.crypto.mac.hmac.HMac
import org.ton.mnemonic.Mnemonic.DEFAULT_BASIC_ITERATIONS
import org.ton.mnemonic.Mnemonic.DEFAULT_BASIC_SALT
import org.ton.mnemonic.Mnemonic.DEFAULT_PASSWORD_ITERATIONS
import org.ton.mnemonic.Mnemonic.DEFAULT_PASSWORD_SALT
import org.ton.mnemonic.Mnemonic.DEFAULT_SALT
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.jvm.JvmStatic
import kotlin.random.Random

private val MnemonicGeneratorCoroutineName = CoroutineName("mnemonic-generator")
private val DEFAULT_BASIC_SALT_BYTES = DEFAULT_BASIC_SALT.encodeToByteArray()
private val DEFAULT_PASSWORD_SALT_BYTES = DEFAULT_PASSWORD_SALT.encodeToByteArray()
private val DEFAULT_SALT_BYTES = DEFAULT_SALT.encodeToByteArray()
private val EMPTY_BYTES = ByteArray(0)

public object Mnemonic {
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

    public fun mnemonicWords(): List<String> = MNEMONIC_WORD_LIST

    @OptIn(DelicateCoroutinesApi::class)
    @JvmStatic
    public suspend fun generate(
        password: String = "",
        wordCount: Int = DEFAULT_WORD_COUNT,
        wordlist: List<String> = mnemonicWords(),
        random: Random = SecureRandom
    ): List<String> = suspendCancellableCoroutine { continuation ->
        GlobalScope.launch(
            Dispatchers.Default + MnemonicGeneratorCoroutineName
        ) {
            try {
                val mnemonic = Array(wordCount) { "" }
                val weakRandom = Random(random.nextLong())
                val digest = Digest.sha512()
                val hMac = HMac(digest)
                val passwordEntropy = ByteArray(hMac.macSize)
                val nonPasswordEntropy = ByteArray(hMac.macSize)
                val passwordBytes = password.toByteArray()
                val generator = PKCSS2ParametersGenerator(hMac)
                while (continuation.isActive) {
                    repeat(wordCount) { i ->
                        mnemonic[i] = wordlist.random(weakRandom)
                    }
                    val mnemonicBytes = mnemonic.joinToString(" ").toByteArray()

                    if (password.isNotEmpty()) {
                        entropy(hMac, mnemonicBytes, EMPTY_BYTES, nonPasswordEntropy)
                        if (!(passwordValidation(generator, nonPasswordEntropy) && !basicValidation(
                                generator,
                                nonPasswordEntropy
                            ))
                        ) {
                            continue
                        }
                    }

                    entropy(hMac, mnemonicBytes, passwordBytes, passwordEntropy)
                    if (!basicValidation(generator, passwordEntropy)) {
                        continue
                    }

                    continuation.resume(mnemonic.toList())
                    break
                }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }
    }

    @JvmStatic
    public fun isPasswordNeeded(mnemonic: List<String>): Boolean {
        val entropy = toEntropy(mnemonic)
        return isPasswordSeed(entropy) && !isBasicSeed(entropy)
    }

    @JvmStatic
    public fun isValid(
        mnemonic: List<String>,
        password: String = "",
        wordlist: List<String> = mnemonicWords()
    ): Boolean {
        if (!mnemonic.all { word -> wordlist.contains(word) }) {
            return false
        }

        if (password.isNotEmpty() && !isPasswordNeeded(mnemonic)) {
            return false
        }

        return isBasicSeed(toEntropy(mnemonic, password))
    }

    @JvmStatic
    public fun toSeed(mnemonic: List<String>, password: String = ""): ByteArray {
        val pbdkf2Sha512 = PKCSS2ParametersGenerator(
            digest = Digest.sha512(),
            password = toEntropy(mnemonic, password),
            salt = DEFAULT_SALT_BYTES,
            iterationCount = DEFAULT_ITERATIONS
        )
        return pbdkf2Sha512.generateDerivedParameters(512).sliceArray(0 until 32)
    }

    @JvmStatic
    public fun toEntropy(mnemonic: List<String>, password: String = ""): ByteArray {
        val digest = Digest.sha512()
        val output = ByteArray(digest.digestSize)
        entropy(HMac(digest), mnemonic.joinToString(" ").toByteArray(), password.toByteArray(), output)
        return output
    }

    @JvmStatic
    public fun isBasicSeed(entropy: ByteArray): Boolean =
        basicValidation(PKCSS2ParametersGenerator(Digest.sha512()), entropy)

    @JvmStatic
    public fun isPasswordSeed(entropy: ByteArray): Boolean =
        passwordValidation(PKCSS2ParametersGenerator(Digest.sha512()), entropy)

}

private fun entropy(hMac: HMac, mnemonic: ByteArray, password: ByteArray, output: ByteArray) {
    hMac.init(mnemonic)
    hMac += password
    hMac.build(output)
}

private fun basicValidation(generator: PKCSS2ParametersGenerator, entropy: ByteArray): Boolean {
    generator.init(
        password = entropy,
        salt = DEFAULT_BASIC_SALT_BYTES,
        iterationCount = DEFAULT_BASIC_ITERATIONS
    )
    return generator.generateDerivedParameters(512).first() == 0.toByte()
}

private fun passwordValidation(generator: PKCSS2ParametersGenerator, entropy: ByteArray): Boolean {
    generator.init(
        password = entropy,
        salt = DEFAULT_PASSWORD_SALT_BYTES,
        iterationCount = DEFAULT_PASSWORD_ITERATIONS
    )
    return generator.generateDerivedParameters(512).first() == 1.toByte()
}
