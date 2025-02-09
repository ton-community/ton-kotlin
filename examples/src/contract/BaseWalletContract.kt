package contract

import kotlinx.coroutines.delay
import org.ton.api.pub.PublicKeyEd25519
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AccountInfo
import org.ton.block.AddrStd
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.kotlin.examples.provider.Provider

abstract class BaseWalletContract(
    val workchain: Int = DEFAULT_WORKCHAIN,
    val publicKey: PublicKeyEd25519,
    val provider: Provider
) {
    companion object {
        const val DEFAULT_WORKCHAIN = 0
        const val DEFAULT_WALLET_ID: Int = 698983191
    }

    val walletId = DEFAULT_WALLET_ID + workchain

    val stateInit: StateInit by lazy {
        StateInit(initCodeCell(), initDataCell())
    }

    val address: AddrStd get() = stateInit.address(workchain)

    abstract val maxMessages: Int

    abstract fun initDataCell(): Cell

    abstract fun initCodeCell(): Cell

    suspend fun getState(blockId: TonNodeBlockIdExt? = null): AccountInfo? = provider.getAccountState(address, blockId)

    abstract suspend fun getSeqno(accountInfo: AccountInfo): Int

    suspend fun waitForStateChange(currentState: AccountInfo?): AccountInfo? {
        while (true) {
            val state = provider.getAccountState(address)
            if (state != currentState) {
                return state
            }
            delay(2000)
        }
    }
}