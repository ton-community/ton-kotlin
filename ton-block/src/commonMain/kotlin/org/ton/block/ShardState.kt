@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface ShardState {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<ShardState> = ShardStateTlbCombinator
    }
}

private object ShardStateTlbCombinator : TlbCombinator<ShardState>() {
    val splitState by lazy { SplitState.tlbCodec() }
    val shardStateUnsplit by lazy { ShardStateUnsplit.tlbCodec() }

    override val constructors: List<TlbConstructor<out ShardState> by lazy {
        listOf(splitState, shardStateUnsplit)
    }

    override fun getConstructor(
        value: ShardState
    ): TlbConstructor<out ShardState> = when (value) {
        is ShardStateUnsplit -> shardStateUnsplit
        is SplitState -> splitState
    }
}