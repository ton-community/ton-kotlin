package org.ton.api

import io.ktor.utils.io.core.*
import org.ton.api.adnl.message.AdnlMessageAnswer
import kotlin.random.Random

suspend fun main() {
    val expected = AdnlMessageAnswer(Random.nextBytes(32), byteArrayOf(1, 2, 3))
    val packet = buildPacket {
        AdnlMessageAnswer.encodeBoxed(this, expected)
    }
    val actual = AdnlMessageAnswer.decodeBoxed(packet)
    println(actual)
}
