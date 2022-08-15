@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface BlockCreateStats {
    companion object : TlbCombinatorProvider<BlockCreateStats> by BlockCreateStatsTlbCombinator
}

private object BlockCreateStatsTlbCombinator : TlbCombinator<BlockCreateStats>() {
    val regular = BlockCreateStatsRegular.tlbConstructor()
    val ext = BlockCreateStatsExt.tlbConstructor()

    override val constructors: List<TlbConstructor<out BlockCreateStats>> =
        listOf(regular, ext)

    override fun getConstructor(
        value: BlockCreateStats
    ): TlbConstructor<out BlockCreateStats> = when (value) {
        is BlockCreateStatsRegular -> regular
        is BlockCreateStatsExt -> ext
    }
}
