package org.ton.block.tlb

import org.ton.block.Coins
import org.ton.block.Maybe
import org.ton.block.StorageInfo
import org.ton.block.StorageUsed
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

fun StorageInfo.Companion.tlbCodec(): TlbCodec<StorageInfo> = StorageInfoTlbConstructor()

private class StorageInfoTlbConstructor : TlbConstructor<StorageInfo>(
    schema = "storage_info\$_ used:StorageUsed last_paid:uint32 due_payment:(Maybe Coins) = StorageInfo;"
) {
    private val storageUsedCodec by lazy {
        StorageUsed.tlbCodec()
    }
    private val maybeCoins by lazy {
        Maybe.tlbCodec(Coins.tlbCodec())
    }

    override fun encode(
        cellBuilder: CellBuilder, value: StorageInfo, param: Int, negativeParam: (Int) -> Unit
    ) = cellBuilder {
        storeTlb(value.used, storageUsedCodec)
        storeUInt(value.lastPaid, 32)
        storeTlb(value.duePayment, maybeCoins)
    }

    override fun decode(
        cellSlice: CellSlice, param: Int, negativeParam: (Int) -> Unit
    ): StorageInfo = cellSlice {
        val used = loadTlb(storageUsedCodec)
        val lastPaid = loadUInt(32).toInt()
        val duePayment = loadTlb(maybeCoins)
        StorageInfo(used, lastPaid, duePayment)
    }
}
