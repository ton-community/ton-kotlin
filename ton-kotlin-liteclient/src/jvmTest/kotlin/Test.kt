import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.ton.api.liteserver.LiteServerDesc
import org.ton.api.pub.PublicKeyEd25519
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.crypto.base64
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.functions.LiteServerGetAccountState
import org.ton.lite.client.LiteClient
import org.ton.tlb.loadTlb

class LiteClientTest2 {

    @Test
    fun test() = runBlocking {
        printLastLtHash()
    }

    suspend fun printLastLtHash(address: String = "EQBnRcXDUmnXeAtPcsmGUQRhpvAyca4H0tHJ7edkj_pxNgBB") {
        val liteClient = LiteClient(
            coroutineContext = currentCoroutineContext(),
            LiteServerDesc(
                id = PublicKeyEd25519(base64("n4VDnSCUuSpjnCyUk9e3QOOd6o0ItSWYbTnW3Wnn8wk=")),
                ip = 84478511,
                port = 19949
            )
        )
        val addrStd = AddrStd(address)
        val accountId = LiteServerAccountId(addrStd.workchainId, addrStd.address)

        val lastBlock = liteClient.getLastBlockId()
        val accountState = liteClient.liteApi(
            LiteServerGetAccountState(
                id = lastBlock,
                account = accountId
            )
        )

        BagOfCells(accountState.proof).first().beginParse().run {
            val slice = loadRef().beginParse()
            val shardState = slice.loadTlb(ShardState)
            println(shardState)

            when (shardState) {
                is SplitState -> TODO()
                is ShardStateUnsplit -> {
                    val dictionaryRoot =
                        shardState.accounts.value.x as AugDictionaryRoot<ShardAccount, DepthBalanceInfo>

                    // Here we will get "java.lang.IndexOutOfBoundsException: Empty list doesn't contain element at index 0."
                    // at loading some AugDictionaryNodeFork
                    dictionaryRoot.root.value.nodes().forEach(::println)
                }
            }
        }
    }
}
