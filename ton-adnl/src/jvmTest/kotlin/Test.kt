import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import org.ton.adnl.client.AdnlSocketConfig
import org.ton.adnl.client.adnl
import org.ton.adnl.ipv4
import org.ton.api.pub.PublicKeyEd25519
import org.ton.crypto.base64

val ipv4 = ipv4(1426768764)
val port = 13724
val publicKey = PublicKeyEd25519(base64("R1KsqYlNks2Zows+I9s4ywhilbSevs9dH1x2KF9MeSU="))

suspend fun main() {
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val socket = aSocket(selectorManager)
        .tcp()
        .connect(ipv4, port)
        .adnl(AdnlSocketConfig(publicKey), Dispatchers.Default)
    val connection = socket.connection()

    delay(10000)
}
