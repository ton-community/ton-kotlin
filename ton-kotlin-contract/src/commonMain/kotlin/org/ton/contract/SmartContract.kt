package org.ton.contract

import org.ton.block.AddrStd
import org.ton.block.Message
import org.ton.block.StateInit
import org.ton.block.VmStack
import org.ton.boc.BagOfCells
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.buildCell
import org.ton.lite.api.LiteApi
import org.ton.lite.api.liteserver.LiteServerAccountId
import org.ton.lite.api.liteserver.functions.LiteServerGetMasterchainInfo
import org.ton.lite.api.liteserver.functions.LiteServerRunSmcMethod
import org.ton.lite.api.liteserver.functions.LiteServerSendMessage
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.storeTlb

public interface SmartContract {
    public val address: AddrStd

    public suspend fun sendExternalMessage(liteApi: LiteApi, message: Message<Cell>): Int =
        sendExternalMessage(liteApi, CellBuilder.createCell {
            storeTlb(Message.tlbCodec(AnyTlbConstructor), message)
        })

    public suspend fun sendExternalMessage(liteApi: LiteApi, message: Cell): Int =
        liteApi(LiteServerSendMessage(BagOfCells(message).toByteArray())).status

    public suspend fun runGetMethod(liteApi: LiteApi, method: String): SmartContractAnswer {
        val lastBlockId = liteApi(LiteServerGetMasterchainInfo).last
        val result = liteApi(
            LiteServerRunSmcMethod(
                mode = 4,
                id = lastBlockId,
                account = LiteServerAccountId(address.workchainId, address.address),
                methodId = LiteServerRunSmcMethod.methodId(method),
                params = LiteServerRunSmcMethod.params()
            )
        )
        return SmartContractAnswer(
            success = result.exitCode == 0,
            stack = result.result?.let {
                VmStack.loadTlb(BagOfCells(it).first())
            }
        )
    }

    public companion object {
        public fun address(workchain: Int, stateInit: StateInit): AddrStd =
            AddrStd(workchain, buildCell { storeTlb(StateInit, stateInit) }.hash())
    }
}


public data class SmartContractAnswer(
    val success: Boolean,
    val stack: VmStack?
)
