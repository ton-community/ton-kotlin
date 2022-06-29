import kotlinx.coroutines.delay
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.Block
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.time.Instant
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
suspend fun main() {
    val liteClient = LiteClient(
        ipv4 = 908566172,
        port = 51565,
        publicKey = base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="),
        logger = PrintLnLogger("TON LiteClient", Logger.Level.INFO)
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")

    var blockId: TonNodeBlockIdExt? = null
    while (true) {
        val currentBlockId = liteClient.getMasterchainInfo().last
        if (blockId != currentBlockId) {
            blockId = currentBlockId
            val (block, duration) = liteClient.getBlock(currentBlockId).dataBagOfCells().roots.first().parse {
                measureTimedValue {
                    Block.TlbCombinator.loadTlb(this)
                }
            }
            println("${block.info.seq_no} $duration")
        }
        delay(1000)
    }
}