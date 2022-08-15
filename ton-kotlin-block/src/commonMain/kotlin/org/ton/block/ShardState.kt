@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface ShardState {
    companion object : TlbCombinatorProvider<ShardState> by ShardStateTlbCombinator
}

private object ShardStateTlbCombinator : TlbCombinator<ShardState>() {
    val splitState = SplitState.tlbConstructor()
    val shardStateUnsplit = ShardStateUnsplit.tlbConstructor()

    override val constructors: List<TlbConstructor<out ShardState>> by lazy {
        listOf(splitState, shardStateUnsplit)
    }

    override fun getConstructor(
        value: ShardState
    ): TlbConstructor<out ShardState> = when (value) {
        is ShardStateUnsplit -> shardStateUnsplit
        is SplitState -> splitState
    }
}
