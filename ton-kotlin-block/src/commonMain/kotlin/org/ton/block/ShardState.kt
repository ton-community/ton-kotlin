@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@JsonClassDiscriminator("@type")
sealed interface ShardState {
    companion object : TlbCombinatorProvider<ShardState> by ShardStateTlbCombinator
}

private object ShardStateTlbCombinator : TlbCombinator<ShardState>(
    ShardState::class,
    SplitState::class to SplitState.tlbConstructor(),
    ShardStateUnsplit::class to ShardStateUnsplit.tlbConstructor(),
)
