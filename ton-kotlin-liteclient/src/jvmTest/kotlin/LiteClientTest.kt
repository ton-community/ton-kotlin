import kotlinx.coroutines.Dispatchers
import org.ton.lite.client.LiteClient

val liteClient = LiteClient(
    coroutineContext = Dispatchers.Default,
    liteClientConfigGlobal = GLOBAL_CONFIG
)
