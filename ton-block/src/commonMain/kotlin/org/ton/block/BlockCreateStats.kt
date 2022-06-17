@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface BlockCreateStats {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<BlockCreateStats> = BlockCreateStatsTlbCombinator
    }
}

private object BlockCreateStatsTlbCombinator : TlbCombinator<BlockCreateStats>() {
    val regular by lazy { BlockCreateStatsRegular.tlbCodec() }
    val ext by lazy { BlockCreateStatsExt.tlbCodec() }

    override val constructors: List<TlbConstructor<out BlockCreateStats>> by lazy {
        listOf(regular, ext)
    }

    override fun getConstructor(
        value: BlockCreateStats
    ): TlbConstructor<out BlockCreateStats> = when(value) {
        is BlockCreateStatsRegular -> regular
        is BlockCreateStatsExt -> ext
    }
}