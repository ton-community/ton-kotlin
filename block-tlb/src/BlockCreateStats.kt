@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@JsonClassDiscriminator("@type")
public sealed interface BlockCreateStats : TlbObject {
    public companion object : TlbCombinatorProvider<BlockCreateStats> by BlockCreateStatsTlbCombinator
}

private object BlockCreateStatsTlbCombinator : TlbCombinator<BlockCreateStats>(
    BlockCreateStats::class,
    BlockCreateStatsRegular::class to BlockCreateStatsRegular.tlbConstructor(),
    BlockCreateStatsExt::class to BlockCreateStatsExt.tlbConstructor()
)
