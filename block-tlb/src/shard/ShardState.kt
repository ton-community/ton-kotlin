@file:Suppress("OPT_IN_USAGE")

package org.ton.block.shard

import kotlinx.serialization.Serializable

import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable

public sealed interface ShardState {
    public companion object : TlbCombinatorProvider<ShardState> by ShardStateTlbCombinator
}

private object ShardStateTlbCombinator : TlbCombinator<ShardState>(
    ShardState::class,
    ShardStateSplit::class to ShardStateSplit.tlbConstructor(),
    ShardStateUnsplit::class to ShardStateUnsplit.tlbConstructor(),
)
