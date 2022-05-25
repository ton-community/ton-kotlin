
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import org.ton.block.MsgAddressInt
import org.ton.crypto.hex
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerAccountState
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.time.Instant

suspend fun main() = coroutineScope {
    val liteClient = LiteClient(
        host = "5.9.10.47",
        port = 19949,
        publicKey = hex("9f85439d2094b92a639c2c9493d7b740e39dea8d08b525986d39d6dd69e7f309"),
        logger = PrintLnLogger("TON LiteClient", Logger.Level.DEBUG)
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")

    val account = MsgAddressInt.AddrStd.parse("0:0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA")
    liteClient.getTransactions(account)
}

private suspend fun LiteClient.getTransactions(addressInt: MsgAddressInt.AddrStd) {
    val masterchainInfo = getMasterchainInfo()
    val lastBlock = masterchainInfo.last
    val accountState = getAccountState(lastBlock, LiteServerAccountId(addressInt))
    Json.encodeToString(LiteServerAccountState.serializer(), accountState)
    println(accountState)
}
