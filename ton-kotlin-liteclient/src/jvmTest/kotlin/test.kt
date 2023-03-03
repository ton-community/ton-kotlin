import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.ton.api.tonnode.Shard
import org.ton.api.tonnode.TonNodeBlockId
import org.ton.lite.client.LiteClient

fun main() = runBlocking {
    val liteClient = LiteClient(
        coroutineContext = Dispatchers.Default,
        liteClientConfigGlobal = GLOBAL_CONFIG
    )
    val blockId = 27750192
    val block = liteClient.getBlock(TonNodeBlockId(-1, Shard.ID_ALL, 27750192)) ?: error("no block")
//    println(block)
    val accountBlocks = block.extra.value.accountBlocks.value
}
