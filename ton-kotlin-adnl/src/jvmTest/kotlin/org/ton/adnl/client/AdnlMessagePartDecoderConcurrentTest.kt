//package org.ton.adnl.client
//
//import org.jetbrains.kotlinx.lincheck.RandomProvider
//import org.jetbrains.kotlinx.lincheck.annotations.Operation
//import org.jetbrains.kotlinx.lincheck.annotations.Param
//import org.jetbrains.kotlinx.lincheck.check
//import org.jetbrains.kotlinx.lincheck.paramgen.ParameterGenerator
//import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
//import org.ton.adnl.peer.AdnlMessagePartDecoder
//import org.ton.api.adnl.message.AdnlMessagePart
//import org.ton.crypto.digest.sha256
//import kotlin.test.Test
//
//@Param(name = "part", gen = PartGenerator::class)
//class AdnlMessagePartDecoderConcurrentTest {
//    private val decoder = AdnlMessagePartDecoder()
//
//    @Operation
//    fun add(@Param(name = "part") part: AdnlMessagePart) = decoder.decode(part)
//
//    @Test
//    fun test() = StressOptions().check(this::class)
//}
//
//class PartGenerator(
//    val randomProvider: RandomProvider,
//    val conf: String
//) : ParameterGenerator<AdnlMessagePart> {
//    val random = randomProvider.createRandom()
//    val byteArray = ByteArray(100) { it.toByte() }
//    val hash = sha256(byteArray)
//
//    private val array = Array(10) {
//        AdnlMessagePart(hash, byteArray.size, it * 10, byteArray.copyOfRange(it * 10, it * 10 + 10))
//    }
//
//    override fun generate(): AdnlMessagePart = array.random(random)
//}
