import org.ton.tonlib.TonlibClient
import org.ton.tonlib.functions.Init
import org.ton.tonlib.types.Config
import org.ton.tonlib.types.KeyStoreType
import org.ton.tonlib.types.Options
import org.ton.tonlib.types.Sync

suspend fun main() {
    val client = TonlibClient()
    val info = client.invoke(
        Init(
            options = Options(
                config = Config(
                    config = GLOBAL_CONFIG,
                    blockchainName = "",
                    useCallbacksForNetwork = false,
                    ignoreCache = false
                ),
                keystoreType = KeyStoreType.IN_MEMORY
            )
        )
    )
    println(info)
    val block = client.invoke(Sync)
    println(block)
}
