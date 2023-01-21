import io.ktor.util.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.lite.client.LiteClient

suspend fun main() {
    val liteClient = LiteClient(
        CoroutineName("liteClient"),
        LiteServerDesc(
            ip = 1091931625,
            port = 30131,
            id = PublicKeyEd25519("wrQaeIFispPfHndEBc0s0fx7GSp8UFFvebnytQQfc6A=".decodeBase64Bytes())
        )
    )

    var id: TonNodeBlockIdExt? = null
    while (true) {
        val currentId = liteClient.getLastBlockId()
        if (currentId == id) {
            delay(1000)
            continue
        }
        id = currentId
        println(currentId)
        val block = liteClient.getBlock(id)
        if (block != null) {
            println(block.extra.account_blocks.value.nodes().map { it.first.account_addr }.toList())
        }
    }
}
