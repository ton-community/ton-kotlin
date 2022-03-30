package ton.crypto

import kotlinx.benchmark.*
import kotlinx.benchmark.Benchmark
import kotlin.random.Random

@State(Scope.Benchmark)
@Measurement(iterations = 3, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@OutputTimeUnit(BenchmarkTimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
internal open class Benchmark {
    lateinit var seed: ByteArray
    lateinit var random: ByteArray
    lateinit var keyPair: Crypto.KeyPair
    lateinit var msg: ByteArray
    lateinit var sig: ByteArray

    @Setup
    fun setup() {
        seed = Random.nextBytes(32)
        random = Random.nextBytes(64)
        keyPair = Crypto.generateKeyPair(seed)
        msg = ByteArray(256)
        sig = Crypto.sign(keyPair.privateKey, msg, null)
    }

    @Benchmark
    fun sign() = Crypto.sign(keyPair.privateKey, msg, null)

    @Benchmark
    fun signRandomized() = Crypto.sign(keyPair.privateKey, msg, random)

    @Benchmark
    fun verify() = Crypto.verify(keyPair.publicKey, msg, sig)

    @Benchmark
    fun generateKeyPair() = Crypto.generateKeyPair(seed)

    @Benchmark
    fun sharedKey() = Crypto.sharedKey(keyPair.publicKey, keyPair.privateKey)
}