package org.ton.lite.api

import io.ktor.utils.io.core.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.*
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.lite.api.liteserver.*
import org.ton.tl.TlConstructor

interface LiteApi {
    suspend fun sendRawQuery(byteArray: ByteArray): ByteArray

    suspend fun <Q : Any, A : Any> sendQuery(query: Q, queryCodec: TlConstructor<Q>, answerCodec: TlConstructor<A>): A {
        val queryBytes = queryCodec.encodeBoxed(query)
        val liteServerQuery = LiteServerQuery(queryBytes)
        val liteServerQueryBytes = LiteServerQuery.encodeBoxed(liteServerQuery)
        val answerBytes = sendRawQuery(liteServerQueryBytes)
        val errorByteInput = ByteReadPacket(answerBytes)
        if (errorByteInput.readIntLittleEndian() == LiteServerError.id) {
            throw LiteServerError.decode(errorByteInput)
        }
        return answerCodec.decodeBoxed(answerBytes)
    }

    suspend fun getTime(): LiteServerCurrentTime =
        sendQuery(LiteServerGetTime, LiteServerGetTime, LiteServerCurrentTime)

    suspend fun getMasterchainInfo(): LiteServerMasterchainInfo =
        sendQuery(LiteServerGetMasterchainInfo, LiteServerGetMasterchainInfo, LiteServerMasterchainInfo)

    suspend fun sendMessage(message: Message<Cell>): LiteServerSendMsgStatus =
        sendMessage(LiteServerSendMessage(message))

    suspend fun sendMessage(bagOfCells: BagOfCells): LiteServerSendMsgStatus =
        sendMessage(LiteServerSendMessage(bagOfCells))

    suspend fun sendMessage(body: ByteArray): LiteServerSendMsgStatus =
        sendMessage(LiteServerSendMessage(body))

    suspend fun sendMessage(query: LiteServerSendMessage): LiteServerSendMsgStatus =
        sendQuery(query, LiteServerSendMessage, LiteServerSendMsgStatus)

    suspend fun getAccountState(id: TonNodeBlockIdExt, account: MsgAddressIntStd): LiteServerAccountState =
        getAccountState(id, LiteServerAccountId(account))

    suspend fun getAccountState(id: TonNodeBlockIdExt, account: LiteServerAccountId): LiteServerAccountState =
        getAccountState(LiteServerGetAccountState(id, account))

    suspend fun getAccountState(query: LiteServerGetAccountState): LiteServerAccountState =
        sendQuery(query, LiteServerGetAccountState, LiteServerAccountState)

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodId: Long,
        params: ByteArray
    ): LiteServerRunMethodResult = runSmcMethod(LiteServerRunSmcMethod(mode, id, account, methodId, params))

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        params: BagOfCells
    ): LiteServerRunMethodResult = runSmcMethod(LiteServerRunSmcMethod(mode, id, account, methodName, params))

    suspend fun runSmcMethod(
        mode: Int,
        id: TonNodeBlockIdExt,
        account: LiteServerAccountId,
        methodName: String,
        vararg params: VmStackValue
    ): LiteServerRunMethodResult = runSmcMethod(
        LiteServerRunSmcMethod(
            mode, id, account, methodName, VmStack(
                VmStackList(params.asIterable())
            )
        )
    )

    suspend fun runSmcMethod(query: LiteServerRunSmcMethod): LiteServerRunMethodResult =
        sendQuery(query, LiteServerRunSmcMethod, LiteServerRunMethodResult)
}
