import org.ton.block.Block
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.time.Instant

suspend fun main() {
    val liteClient = LiteClient(
        ipv4 = 908566172,
        port = 51565,
        publicKey = base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="),
        logger = PrintLnLogger("TON LiteClient", Logger.Level.DEBUG)
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")

    var referenceBlock = liteClient.getMasterchainInfo().last
    liteClient.getBlock(referenceBlock).dataBagOfCells().roots.first().parse {
        Block.TlbCombinator.loadTlb(this)
    }.let {
        println(it)
    }

}