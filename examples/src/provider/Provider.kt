package org.ton.kotlin.examples.provider

import io.ktor.util.*
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.AccountInfo
import org.ton.block.Message
import org.ton.block.MsgAddressInt
import org.ton.boc.BagOfCells
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.buildCell
import org.ton.lite.client.LiteClient
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbStorer
import org.ton.tlb.storeTlb

interface Provider {
    suspend fun getAccountState(address: MsgAddressInt, blockId: TonNodeBlockIdExt? = null): AccountInfo?

    suspend fun <T : Any> sendMessage(serializer: TlbStorer<T>, message: Message<T>)
}

class LiteClientProvider(
    val liteClient: LiteClient
) : Provider {
    override suspend fun getAccountState(
        address: MsgAddressInt,
        blockId: TonNodeBlockIdExt?
    ): AccountInfo? {
        val state = if (blockId == null) {
            liteClient.getAccountState(address)
        } else {
            liteClient.getAccountState(address, blockId)
        }
        return state.account.value as? AccountInfo
    }

    override suspend fun <T : Any> sendMessage(serializer: TlbStorer<T>, message: Message<T>) {
        val cell = buildCell {
            val codec = Message.tlbCodec<T>(object : TlbCodec<T> {
                override fun storeTlb(cellBuilder: CellBuilder, value: T) = serializer.storeTlb(cellBuilder, value)
                override fun loadTlb(cellSlice: CellSlice): T = throw UnsupportedOperationException()
            })
            storeTlb(codec, message)
        }
        val byteArray = BagOfCells(cell).toByteArray()
        println(hex(byteArray))
        val bagOfCells = BagOfCells(byteArray).first()
        println(bagOfCells)
        println(cell)
    }
}