import kotlinx.coroutines.delay
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AccountBlock
import org.ton.block.AddrStd
import org.ton.block.Block
import org.ton.cell.Cell
import org.ton.crypto.base64
import org.ton.lite.client.LiteClient
import org.ton.logger.Logger
import org.ton.logger.PrintLnLogger
import java.time.Instant

suspend fun main() {
    val liteClient = LiteClient(
        ipv4 = 908566172,
        port = 51565,
        publicKey = base64("TDg+ILLlRugRB4Kpg3wXjPcoc+d+Eeb7kuVe16CS9z8="),
        logger = PrintLnLogger("TON LiteClient", Logger.Level.DEBUG)
    ).connect()
    val time = liteClient.getTime()
    println("[server time: $time] (${Instant.ofEpochSecond(time.now.toLong())})")
    transactionsRealtime(liteClient)
}

private suspend fun transactionsRealtime(liteClient: LiteClient) {
    var lastSeqno = -1
    while (true) {
        val blockId = liteClient.getMasterchainInfo().last
        if (blockId.seqno == lastSeqno) {
            delay(2500)
            continue
        }
        lastSeqno = blockId.seqno
        println("[$lastSeqno]")
        val accountBlocks = liteClient.getAccountBlocks(blockId)
        accountBlocks.forEach { (addr, account) ->
            val inMsgCount = account.transactions.count { it.first.in_msg.value != null }
            val outMsgCount = account.transactions.sumOf { it.first.out_msgs.count() }
            println("  ${addr.toString(true)} - in:$inMsgCount out:$outMsgCount")
        }
    }
}

/**
 * Get recursively account blocks by block id
 */
private suspend fun LiteClient.getAccountBlocks(blockId: TonNodeBlockIdExt): Map<AddrStd, AccountBlock> {
    val block = getBlock(blockId).toBlock()
    val accounts = LinkedHashMap<AddrStd, AccountBlock>()
    accounts.putAll(block.accountBlocks(blockId.workchain))
    block.collectWorkchainBlockIds().forEach {
        accounts.putAll(getAccountBlocks(it))
    }
    return accounts
}

private fun Block.accountBlocks(workchain: Int): Map<AddrStd, AccountBlock> =
    extra.account_blocks.associate {
        AddrStd(workchain, it.first.account_addr) to it.first
    }

/**
 * Collects workchain block ids
 */
private fun Block.collectWorkchainBlockIds(): List<TonNodeBlockIdExt> {
    val extraValue = extra.custom.value ?: return emptyList()
    return extraValue.shard_hashes.flatMap { (rawWorkchain, descriptions) ->
        val workchain = Cell(rawWorkchain).beginParse().loadInt(32).toInt()
        descriptions.map { descr ->
            TonNodeBlockIdExt(
                workchain,
                descr.next_validator_shard,
                descr.seq_no.toInt(),
                descr.root_hash,
                descr.file_hash
            )
        }
    }
}
