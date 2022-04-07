package ton.lite.client

import ton.adnl.AdnlClient
import ton.adnl.AdnlPublicKey
import ton.crypto.hex
import java.time.Instant
import kotlin.random.Random

suspend fun main() {
    connectAndSend()
}

private suspend fun connectAndSend() {
    val adnlClient = AdnlClient(
        serverPublicKey = AdnlPublicKey(hex("a5e253c3f6ab9517ecb204ee7fd04cca9273a8e8bb49712a48f496884c365353")),
        host = "67.207.74.182",
        port = 4924,
    ).connect()

    val query = AdnlMessageQuery.encodeBoxed(
        AdnlMessageQuery(
            queryId = LongArray(4) { Random.nextLong() },
            query = LiteServerQuery.encodeBoxed(
                LiteServerQuery(
                    data = LiteServerGetTime.encodeBoxed(LiteServerGetTime())
                )
            )
        )
    )
    adnlClient.send(query)
    val adnlMessageAnswer = AdnlMessageAnswer.decodeBoxed(adnlClient.receive())
    val liteServerCurrentTime = LiteServerCurrentTime.decodeBoxed(adnlMessageAnswer.answer)
    val serverTime = liteServerCurrentTime.now
    println("server time: $serverTime (${Instant.ofEpochSecond(serverTime)})")
}