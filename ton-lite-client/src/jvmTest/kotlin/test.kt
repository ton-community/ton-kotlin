
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import org.ton.block.AddrStd
import org.ton.boc.BagOfCells
import org.ton.crypto.base64
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerAccountState
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.time.Instant

suspend fun main() = coroutineScope {
    val liteClient = LiteClient(
        ipv4 = 84478479,
        port = 48014,
        publicKey = base64("3XO67K/qi+gu3T9v8G2hx1yNmWZhccL3O7SoosFo8G0="),
        logger = PrintLnLogger("TON LiteClient", Logger.Level.DEBUG)
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")

    val id = liteClient.getMasterchainInfo().last
    val block = liteClient.getBlock(id)
    val bagOfCells = BagOfCells(block.data)
    println(bagOfCells)
//    val file = File("C:\\Users\\andreypfau\\Desktop\\block.boc")
//    file.writeBytes(block.data)
//    println(block.dataBagOfCells())

//    val account = MsgAddressIntStd.parse("0:0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA")
//    liteClient.getTransactions(account)
}

private suspend fun LiteClient.getTransactions(addressInt: AddrStd) {
    val masterchainInfo = getMasterchainInfo()
    val lastBlock = masterchainInfo.last
    val accountState = getAccountState(lastBlock, LiteServerAccountId(addressInt))
    Json.encodeToString(LiteServerAccountState.serializer(), accountState)
    println(accountState)
}
