package org.ton.lite.client

import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.ton.adnl.AdnlPublicKey
import org.ton.adnl.AdnlTcpClient
import org.ton.adnl.AdnlTcpClientImpl
import org.ton.cell.BagOfCells
import org.ton.cell.writeBagOfCells
import org.ton.crypto.hex
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerMasterchainInfo
import java.time.Instant

suspend fun main() = coroutineScope {
    val liteClient = LiteClient(
            host = "67.207.74.182",
            port = 4924,
            publicKey = hex("a5e253c3f6ab9517ecb204ee7fd04cca9273a8e8bb49712a48f496884c365353")
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")

    val tr = liteClient.getAccountTransactions(0, "0AB558F4DB84FD31F61A273535C670C091FFC619B1CDBBE5769A0BF28D3B8FEA")
    println(tr)
}


suspend fun LiteClient.getAccountTransactions(workchain: Int, address: String): String {
    val lastBlock = getMasterchainInfo().last
    val state = getAccountState(lastBlock, LiteServerAccountId(workchain, hex(address)))
    val cell = BagOfCells(state.state).roots.first()


    println(hex(state.state))
    println(hex(buildPacket { writeBagOfCells(BagOfCells(cell)) }.readBytes()))

//    val json = cell.slice().Account().toJsonString()
    return "json"
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
        val adnlTcpClient: AdnlTcpClient
) : LiteApi {
    constructor(
            host: String,
            port: Int,
            publicKey: ByteArray
    ) : this(AdnlTcpClientImpl(host, port, AdnlPublicKey(publicKey), Dispatchers.Default))

    suspend fun connect() = apply {
        adnlTcpClient.connect()
    }

    override suspend fun sendRawQuery(byteArray: ByteArray): ByteArray =
            adnlTcpClient.sendQuery(byteArray)
}