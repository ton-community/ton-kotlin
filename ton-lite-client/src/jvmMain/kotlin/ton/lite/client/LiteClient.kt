package ton.lite.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.JsonObject
import ton.adnl.AdnlClient
import ton.adnl.AdnlPublicKey
import ton.cell.BagOfCells
import ton.cell.slice
import ton.crypto.hex
import ton.tlb.Account
import ton.types.block.Account
import java.time.Instant

suspend fun main() = coroutineScope {
    val liteClient = LiteClient(
        host = "67.207.74.182",
        port = 4924,
        publicKey = hex("a5e253c3f6ab9517ecb204ee7fd04cca9273a8e8bb49712a48f496884c365353")
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now)})")

    val tr = liteClient.getAccountTransactions(0, "0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA")
    println(tr)
}


suspend fun LiteClient.getAccountTransactions(workchain: Int, address: String): Account? {
    val lastBlock = getMasterchainInfo().last
    val state = getAccountState(lastBlock, LiteServerAccountId(workchain, hex(address)))
    val cell = BagOfCells(state.state).roots.first()
    val json = cell.slice().Account().toJsonElement() as JsonObject
    TODO()
}

suspend fun LiteClient.lastBlockTask() = coroutineScope {
    var lastBlock: LiteServerMasterchainInfo? = null
    while (isActive) {
        val currentBlock = getMasterchainInfo()
        if (currentBlock != lastBlock) {
            lastBlock = currentBlock
            println("[${Instant.now()}] $currentBlock")
        }
        delay(1000)
    }
}

class LiteClient(
    host: String,
    port: Int,
    publicKey: ByteArray,
) : LiteServerApi {
    override val adnlClient = AdnlClient(host, port, AdnlPublicKey(publicKey), Dispatchers.Default)

    suspend fun connect() = apply {
        adnlClient.connect()
    }
}