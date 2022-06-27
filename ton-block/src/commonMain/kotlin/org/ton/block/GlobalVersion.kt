package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor

@SerialName("capabilities")
@Serializable
data class GlobalVersion(
    val version: Long,
    val capabilities: Long
) {
    companion object : TlbCodec<GlobalVersion> by GlobalVersionTlbConstructor.asTlbCombinator()
}

private object GlobalVersionTlbConstructor : TlbConstructor<GlobalVersion>(
    schema = "capabilities#c4 version:uint32 capabilities:uint64 = GlobalVersion;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: GlobalVersion
    ) = cellBuilder {
        storeUInt(value.version, 32)
        storeUInt(value.capabilities, 64)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): GlobalVersion = cellSlice {
        val version = loadUInt(32).toLong()
        val capabilities = loadUInt(64).toLong()
        GlobalVersion(version, capabilities)
    }
}