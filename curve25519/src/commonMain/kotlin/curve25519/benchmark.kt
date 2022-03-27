package curve25519

import kotlinx.benchmark.*
import kotlinx.benchmark.Benchmark

@State(Scope.Benchmark)
@Measurement(iterations = 3, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@OutputTimeUnit(BenchmarkTimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
internal open class Benchmark {
    lateinit var seed: IntArray
    lateinit var random: IntArray
    lateinit var keyPair: Curve25519.KeyPair
    lateinit var msg: IntArray
    lateinit var sig: IntArray

    @Setup
    fun setup() {
        seed = Curve25519.randomBytes(32)
        random = Curve25519.randomBytes(64)
        keyPair = Curve25519.generateKeyPair(seed)
        msg = IntArray(256)
        sig = Curve25519.sign(keyPair.privateKey, msg, null)
    }

    @Benchmark
    fun sign() = Curve25519.sign(keyPair.privateKey, msg, null)

    @Benchmark
    fun signRandomized() = Curve25519.sign(keyPair.privateKey, msg, random)

    @Benchmark
    fun verify() = Curve25519.verify(keyPair.publicKey, msg, sig)

    @Benchmark
    fun generateKeyPair() = Curve25519.generateKeyPair(seed)

    @Benchmark
    fun sharedKey() = Curve25519.sharedKey(keyPair.publicKey, keyPair.privateKey)
}