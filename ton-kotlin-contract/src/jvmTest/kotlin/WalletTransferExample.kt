import org.ton.api.pk.PrivateKeyEd25519
import org.ton.block.Coins
import org.ton.contract.wallet.MessageData
import org.ton.contract.wallet.WalletV3R2Contract
import org.ton.contract.wallet.liteClient
import org.ton.contract.wallet.transfer
import kotlin.random.Random

suspend fun main() {
    val liteClient = liteClient()
    val privateKey = PrivateKeyEd25519(Random(0))
    val wallet = WalletV3R2Contract(liteClient, WalletV3R2Contract.address(privateKey))

    println("Wallet Address: ${wallet.address.toString(userFriendly = true)}")

    wallet.transfer(privateKey) {
        coins = Coins.ofNano(12)
        messageData = MessageData.text("Hello World!")
    }
}
