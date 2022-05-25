package org.ton.lite.client

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.ton.adnl.AdnlPublicKey
import org.ton.adnl.AdnlTcpClient
import org.ton.adnl.AdnlTcpClientImpl
import org.ton.cell.BagOfCells
import org.ton.crypto.base64
import org.ton.crypto.hex
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.LiteServerMasterchainInfo
import java.time.Instant

suspend fun main() = coroutineScope {
    val liteClient = LiteClient(
        host = "185.86.79.9",
        port = 4701,
        publicKey = base64("G6cNAr6wXBBByWDzddEWP5xMFsAcp6y13fXA8Q7EJlM=")
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


    return cell.toString()
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
