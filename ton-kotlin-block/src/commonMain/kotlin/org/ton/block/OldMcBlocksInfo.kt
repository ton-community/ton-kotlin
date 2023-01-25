package org.ton.block

import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.hashmap.AugDictionary
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbObject
import org.ton.tlb.TlbPrettyPrinter
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class OldMcBlocksInfo(
    public val value: AugDictionary<KeyExtBlkRef, KeyMaxLt>
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return value.print(printer)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCodec<OldMcBlocksInfo> by OldMcBlocksInfoTlbCodec
}

private object OldMcBlocksInfoTlbCodec : TlbCodec<OldMcBlocksInfo> {
    private val codec = AugDictionary.tlbCodec(32, KeyExtBlkRef, KeyMaxLt)

    override fun storeTlb(cellBuilder: CellBuilder, value: OldMcBlocksInfo) {
        codec.storeTlb(cellBuilder, value.value)
    }

    override fun loadTlb(cellSlice: CellSlice): OldMcBlocksInfo {
        return OldMcBlocksInfo(codec.loadTlb(cellSlice))
    }
}
