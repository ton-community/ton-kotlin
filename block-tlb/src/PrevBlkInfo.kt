package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb

@Serializable
@SerialName("prev_blk_info")
public data class PrevBlkInfo(
    val prev: ExtBlkRef // prev : ExtBlkRef
) : BlkPrevInfo {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("prev_blk_info") {
        field("prev", prev)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<PrevBlkInfo> by PrevBlkInfoTlbConstructor
}

private object PrevBlkInfoTlbConstructor : TlbConstructor<PrevBlkInfo>(
    schema = "prev_blk_info\$_ prev:ExtBlkRef = BlkPrevInfo 0;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: PrevBlkInfo
    ) = cellBuilder {
        storeTlb(ExtBlkRef, value.prev)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): PrevBlkInfo = cellSlice {
        val prev = loadTlb(ExtBlkRef)
        PrevBlkInfo(prev)
    }
}
