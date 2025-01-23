package org.ton.block

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

/**
 * Software info.
 */
public data class GlobalVersion(
    /**
     * Software version.
     */
    val version: Int,

    /**
     * Software capability flags.
     */
    val capabilities: GlobalCapabilities
) {
    public object Tlb : TlbConstructor<GlobalVersion>(
        schema = "capabilities#c4 version:uint32 capabilities:uint64 = GlobalVersion;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: GlobalVersion
        ): Unit = cellBuilder {
            storeUInt(value.version.toInt(), 32)
            GlobalCapabilities.Tlb.storeTlb(this, value.capabilities)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): GlobalVersion = cellSlice {
            val version = loadUInt().toInt()
            val capabilities = GlobalCapabilities.Tlb.loadTlb(this)
            GlobalVersion(version, capabilities)
        }
    }
}

