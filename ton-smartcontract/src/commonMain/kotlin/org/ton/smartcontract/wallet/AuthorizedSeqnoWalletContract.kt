package org.ton.smartcontract.wallet

import org.ton.block.Coins
import org.ton.block.Message
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.lite.api.LiteApi

/**
 * Wallet contract we have a permission to perform operations on, such as transfers etc.
 */
interface AuthorizedSeqnoWalletContract : AuthorizedWalletContract, SeqnoWalletContract {
    /**
     * Prepare a transfer message
     */
    suspend fun createTransferMessage(
        liteApi: LiteApi,
        seqno: Int,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell = Cell.of(),
        bounce: Boolean,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    ): Message<Cell>

    override suspend fun createTransferMessage(
        liteApi: LiteApi,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell,
        bounce: Boolean,
        sendMode: Int,
        destinationStateInit: StateInit?
    ): Message<Cell> =
        createTransferMessage(liteApi, seqno(liteApi), destination, amount, payload, bounce, 3, destinationStateInit)

    /**
     * Perform a transfer of funds
     */
    suspend fun transfer(
        liteApi: LiteApi,
        seqno: Int,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell = Cell.of(),
        bounce: Boolean = true,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    ) {
        val message =
            createTransferMessage(liteApi, seqno, destination, amount, payload, bounce, sendMode, destinationStateInit)
        liteApi.sendMessage(message)
    }

    /**
     * Perform a transfer of funds with a text comment
     */
    suspend fun transfer(
        liteApi: LiteApi,
        seqno: Int,
        destination: MsgAddressInt,
        amount: Coins,
        comment: String,
        bounce: Boolean = true,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    ) {
        transfer(liteApi, seqno, destination, amount, comment, bounce, sendMode, destinationStateInit)
    }

    override suspend fun transfer(
        liteApi: LiteApi,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell,
        bounce: Boolean,
        sendMode: Int,
        destinationStateInit: StateInit?
    ) {
        transfer(liteApi, seqno(liteApi), destination, amount, payload, bounce, sendMode, destinationStateInit)
    }

    override suspend fun transfer(
        liteApi: LiteApi,
        destination: MsgAddressInt,
        amount: Coins,
        comment: String,
        bounce: Boolean,
        sendMode: Int,
        destinationStateInit: StateInit?
    ) {
        transfer(liteApi, seqno(liteApi), destination, amount, comment, bounce, sendMode, destinationStateInit)
    }
}
