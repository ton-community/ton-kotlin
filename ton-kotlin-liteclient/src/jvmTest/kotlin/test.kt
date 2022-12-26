import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import org.ton.adnl.network.IPv4Address
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pk.PrivateKey
import org.ton.api.pk.PrivateKeyEd25519
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import java.io.File

suspend fun main() {
    remote()
//    myLocalServer()
}

suspend fun remote() {
    val liteClient = LiteClient(
        liteClientConfigGlobal = LiteClientConfigGlobal(
            liteServers = listOf(
                LiteServerDesc(
                    id = PublicKeyEd25519(base64("n4VDnSCUuSpjnCyUk9e3QOOd6o0ItSWYbTnW3Wnn8wk=")),
                    ip = 84478511,
                    port = 19949
                )
            )
        ),
        coroutineContext = Dispatchers.Default
    )
    val data = liteClient.getAccount("EQAKtVj024T9MfYaJzU1xnDAkf_GGbHNu-V2mgvyjTuP6rvC")
//    println(data)
}

suspend fun myLocalServer() {
    val file =
        File("/Users/andreypfau/CLionProjects/ton/cmake-build-debug/validator-engine/validator-db/keyring/5CAE14E0D697AC98C36E318DA93F6ABCFEDEFAC83DA7F547630D613BD78072BA").readBytes()
    val pk = PrivateKey.decodeBoxed(file) as PrivateKeyEd25519
    val ip = IPv4Address("127.0.0.1", 4444)
    val liteClient = LiteClient(
        liteClientConfigGlobal = LiteClientConfigGlobal(
            liteServers = listOf(
                LiteServerDesc(
                    id = pk.publicKey(),
                    ip = ip.address,
                    port = ip.port
                )
            )
        ),
        coroutineContext = currentCoroutineContext()
    )
    val data = liteClient.getAccount("EQAs87W4yJHlF8mt29ocA4agnMrLsOP69jC1HPyBUjJay-7l", TonNodeBlockIdExt())
//    println(liteClient.getLastBlockId())
}
