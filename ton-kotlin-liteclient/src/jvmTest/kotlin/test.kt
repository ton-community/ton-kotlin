import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockId
import org.ton.api.validator.config.ValidatorConfigGlobal
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient

fun main() {
    val liteClient = LiteClient(
        coroutineContext = Dispatchers.Default,
        liteClientConfigGlobal = LiteClientConfigGlobal(
            liteServers = listOf(
                LiteServerDesc(
                    id = PublicKeyEd25519(base64("p2tSiaeSqX978BxE5zLxuTQM06WVDErf5/15QToxMYA=")),
                    ip = 1097649206,
                    port = 29296
                )
            ),
            validator = ValidatorConfigGlobal()
        )
    )
    val blockId = 8573000
    runBlocking {
        liteClient.lookupBlock(
            TonNodeBlockId(workchain = 0, shard = Long.MIN_VALUE, seqno = blockId)
        )?.let {
            println(liteClient.getBlock(it))
        }
    }
}
