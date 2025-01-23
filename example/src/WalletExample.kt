import kotlinx.coroutines.currentCoroutineContext
import kotlinx.serialization.json.Json
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.lite.client.LiteClient

suspend fun main() {
    val liteConfig = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<LiteClientConfigGlobal>(TESTNET_CONFIG_JSON)
    val liteClient = LiteClient(currentCoroutineContext(), liteConfig)

    val mcBlock = liteClient.getLastBlockId()
    println(mcBlock)
}