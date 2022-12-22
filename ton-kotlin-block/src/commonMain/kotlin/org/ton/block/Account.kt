@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Account {
    companion object : TlbCombinatorProvider<Account> by AccountTlbCombinator
}

private object AccountTlbCombinator : TlbCombinator<Account>(
    Account::class,
    AccountNone::class to AccountNone,
    AccountInfo::class to AccountInfo
)
