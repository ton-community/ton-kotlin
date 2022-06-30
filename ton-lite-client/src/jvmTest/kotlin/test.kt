@file:OptIn(ExperimentalTime::class)

import kotlinx.coroutines.delay
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AddrStd
import org.ton.block.Block
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.time.Instant
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
suspend fun main() {
    val liteClient = LiteClient(
        ipv4 = 908566172,
        port = 51565,
        publicKey = base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="),
        logger = PrintLnLogger("TON LiteClient", Logger.Level.INFO)
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")

    nft(liteClient)

}

suspend fun nft(liteClient: LiteClient) {
    val masterchainInfo = liteClient.getMasterchainInfo()
    val lastBlock = masterchainInfo.last

    val liteServerAccountState =
        liteClient.getAccountState(lastBlock, AddrStd("EQAKtVj024T9MfYaJzU1xnDAkf_GGbHNu-V2mgvyjTuP6rvC"))
    println(liteServerAccountState.stateToAccount())

    // rawData         : AccountState
    // rawData.refs[0] : Code
    // rawData.refs[1] : Data
//    val storage = accountStateBoC.refs[1].beginParse().loadTlb(AccountStorage.tlbCodec())

//    println("Storage.Balance: ${storage.balance.coins.amount.value}")
//    println("Storage.State: ${storage.state::class}")

//            println("Code: ${rawData.refs[0]}")

//            println("NFT offchain content: ${String(BagOfCells(accountState.state).first().refs.last().refs.first().bits.toByteArray())}")
    // String(BagOfCells(accountState.state).first().refs.last().refs.first().bits.toByteArray()) // offchain json link
    // String(BagOfCells(accountState.state).first().refs.last().refs.first().refs.first().bits.toByteArray()) // nft EQB8zFOBBsgiERJ_MG7k_OPmw101t0ViOyuEry4AtQTLDqM6 , value=3691.json
    // getAccountStorage
}

suspend fun block(liteClient: LiteClient) {
    var blockId: TonNodeBlockIdExt? = null
    while (true) {
        val currentBlockId = liteClient.getMasterchainInfo().last
        if (blockId == currentBlockId) {
            delay(1000)
            continue
        } else {
            blockId = currentBlockId
        }

        val (block, duration) = liteClient.getBlock(currentBlockId).dataBagOfCells().roots.first().parse {
            measureTimedValue {
                Block.TlbCombinator.loadTlb(this)
            }
        }
        println("${block.info.seq_no} $duration")
//        val header = liteClient.getBlockHeader(blockId, 0)
//        println("  header: $header")
    }
}