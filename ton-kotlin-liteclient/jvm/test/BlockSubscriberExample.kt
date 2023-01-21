import io.ktor.util.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockId
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

    val mutableFlow = MutableStateFlow(
        liteClient.getLastBlockId()
    )
    GlobalScope.launch {
        while (true) {
            mutableFlow.update {
                var block: TonNodeBlockIdExt? = null
                while (block == null) {
                    block = liteClient.lookupBlock(
                        TonNodeBlockId(
                            it.workchain,
                            it.shard,
                            it.seqno + 1,
                        )
                    )
                }
                block
            }
        }
    }

    val flow = mutableFlow.asStateFlow()
    flow.collectLatest {
        println(it)
    }
}
