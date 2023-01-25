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
public value class ShardAccounts(
    public val x: AugDictionary<ShardAccount, DepthBalanceInfo>
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter {
        return x.print(printer)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCodec<ShardAccounts> by ShardAccountsTlbCodec
}

private object ShardAccountsTlbCodec : TlbCodec<ShardAccounts> {
    private val codec = AugDictionary.tlbCodec(256, ShardAccount, DepthBalanceInfo)

    override fun storeTlb(cellBuilder: CellBuilder, value: ShardAccounts) {
        codec.storeTlb(cellBuilder, value.x)
    }

    override fun loadTlb(cellSlice: CellSlice): ShardAccounts {
        return ShardAccounts(codec.loadTlb(cellSlice))
    }
}
