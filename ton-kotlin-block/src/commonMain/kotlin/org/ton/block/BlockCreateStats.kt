@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface BlockCreateStats {
    companion object : TlbCombinatorProvider<BlockCreateStats> by BlockCreateStatsTlbCombinator
}

private object BlockCreateStatsTlbCombinator : TlbCombinator<BlockCreateStats>(
    BlockCreateStats::class,
    BlockCreateStatsRegular::class to BlockCreateStatsRegular.tlbConstructor(),
    BlockCreateStatsExt::class to BlockCreateStatsExt.tlbConstructor()
)
