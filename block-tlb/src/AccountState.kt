@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider


@JsonClassDiscriminator("@type")
public sealed interface AccountState : TlbObject {
    /**
     * Account status.
     */
    public val status: AccountStatus

    public companion object : TlbCombinatorProvider<AccountState> by AccountStateTlbCombinator
}

private object AccountStateTlbCombinator : TlbCombinator<AccountState>(
    AccountState::class,
    AccountUninit::class to AccountUninit,
    AccountActive::class to AccountActive,
    AccountFrozen::class to AccountFrozen
)
