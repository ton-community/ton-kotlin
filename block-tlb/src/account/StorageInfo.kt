package org.ton.block.account

import org.ton.block.currency.Coins
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.*

/**
 * Storage profile of an account.
 */
public data class StorageInfo(
    /**
     * Amount of unique cells and bits which account state occupies.
     */
    val used: StorageUsed,

    /**
     * Unix timestamp of the last storage phase.
     */
    val lastPaid: Long,

    /**
     * Account debt for storing its state.
     */
    val duePayment: Coins?
) {
    public object Tlb : TlbCodec<StorageInfo> {
        override fun storeTlb(
            cellBuilder: CellBuilder, value: StorageInfo
        ): Unit = cellBuilder {
            storeTlb(StorageUsed.Tlb, value.used)
            storeUInt32(value.lastPaid.toUInt())
            storeNullableTlb(Coins.Tlb, value.duePayment)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): StorageInfo = cellSlice {
            val used = loadTlb(StorageUsed.Tlb)
            val lastPaid = loadUInt().toLong()
            val duePayment = loadNullableTlb(Coins.Tlb)
            StorageInfo(used, lastPaid, duePayment)
        }
    }
}