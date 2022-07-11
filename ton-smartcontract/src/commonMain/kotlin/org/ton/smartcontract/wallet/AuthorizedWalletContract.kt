package org.ton.smartcontract.wallet

import org.ton.block.Coins
import org.ton.block.Message
import org.ton.block.MsgAddressInt
import org.ton.block.StateInit
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.lite.api.LiteApi

/**
 * Wallet contract we have a permission to perform operations on, such as transfers etc.
 */
interface AuthorizedWalletContract : WalletContract {
    /**
     * Prepare a transfer message
     */
    suspend fun createTransferMessage(
        liteApi: LiteApi,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell = Cell.of(),
        bounce: Boolean = true,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    ): Message<Cell>

    /**
     * Most of the time messages have some sort of text comment attached to them
     */
    fun createCommentPayload(comment: String): Cell {
        val commentBytes = comment.encodeToByteArray()
        require(commentBytes.size <= 123) { TODO("comment is too long: ${commentBytes.size} bytes provided, 123 max supported") }
        return CellBuilder.createCell {
            storeUInt(0, 32) // op == 0 for comments
            storeBytes(commentBytes)
        }
    }

    /**
     * Perform a transfer of funds
     */
    suspend fun transfer(
        liteApi: LiteApi,
        destination: MsgAddressInt,
        amount: Coins,
        payload: Cell = Cell.of(),
        bounce: Boolean = true,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    )

    /**
     * Perform a transfer of funds with a text comment
     */
    suspend fun transfer(
        liteApi: LiteApi,
        destination: MsgAddressInt,
        amount: Coins,
        comment: String,
        bounce: Boolean = true,
        sendMode: Int = 3,
        destinationStateInit: StateInit? = null,
    ) = transfer(liteApi, destination, amount, createCommentPayload(comment), bounce, sendMode, destinationStateInit)

    /**
     * Prepare an external initialization message to deploy the contract
     */
    fun createExternalInitMessage(): Message<Cell>

    /**
     * Use [createExternalInitMessage]'s result to deploy the contract to the network
     */
    suspend fun deploy(liteApi: LiteApi) {
        val initMessage = createExternalInitMessage()
        liteApi.sendMessage(initMessage)
    }
}
