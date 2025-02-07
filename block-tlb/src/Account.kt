@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbObject
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")

public sealed interface Account : TlbObject {
    public companion object : TlbCombinatorProvider<Account> by AccountTlbCombinator
}

private object AccountTlbCombinator : TlbCombinator<Account>(
    Account::class,
    AccountNone::class to AccountNone,
    AccountInfo::class to AccountInfo
)
