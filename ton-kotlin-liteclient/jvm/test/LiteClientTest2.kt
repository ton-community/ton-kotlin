import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ton.api.liteclient.config.LiteClientConfigGlobal
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockId
import org.ton.block.AccountActive
import org.ton.block.AccountInfo
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LiteClientTest2 {

    private fun getClient() = LiteClient(
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

    @Test
    fun getAccount() {
        val liteClient = getClient()
        runBlocking {
            val data = liteClient.getAccount("EQAs87W4yJHlF8mt29ocA4agnMrLsOP69jC1HPyBUjJay-7l")

            // assume that active account is OK
            assertTrue(data is AccountInfo)
            assertNotNull(data.storage.state)
            assertTrue(data.storage.state is AccountActive)
        }
    }
}
