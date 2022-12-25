@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import kotlin.jvm.JvmStatic

@Serializable
@JsonClassDiscriminator("@type")
sealed interface AccountState {
    companion object : TlbCodec<AccountState> by AccountStateTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<AccountState> = AccountStateTlbCombinator
    }
}

private object AccountStateTlbCombinator : TlbCombinator<AccountState>(
    AccountState::class,
    AccountUninit::class to AccountUninit.tlbCodec(),
    AccountActive::class to AccountActive.tlbCodec(),
    AccountFrozen::class to AccountFrozen.tlbCodec()
)
