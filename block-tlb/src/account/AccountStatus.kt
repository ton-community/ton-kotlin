package org.ton.block.account

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec

/**
 * Brief account status.
 */
public enum class AccountStatus {
    /**
     * Account exists but has not yet been deployed.
     */
    Uninit,

    /**
     * Account exists but has been frozen.
     */
    Frozen,

    /**
     * Account exists and has been deployed.
     */
    Active,

    /**
     * Account does not exist.
     */
    NotExist;

    public object Tlb : TlbCodec<AccountStatus> {
        override fun loadTlb(cellSlice: CellSlice): AccountStatus {
            val tag = cellSlice.loadUInt(2).toInt()
            return when (tag) {
                0b00 -> Uninit
                0b01 -> Frozen
                0b10 -> Active
                0b11 -> NotExist
                else -> throw IllegalStateException("Unknown account status type: $tag")
            }
        }

        override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatus) {
            val tag = when (value) {
                Uninit -> 0b00
                Frozen -> 0b01
                Active -> 0b10
                NotExist -> 0b11
            }
            cellBuilder.storeUInt(tag, 2)
        }
    }

    public companion object
}