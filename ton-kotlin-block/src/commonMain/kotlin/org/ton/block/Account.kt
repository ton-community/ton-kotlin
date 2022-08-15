@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Account {
    companion object : TlbCombinatorProvider<Account> by AccountTlbCombinator
}

private object AccountTlbCombinator : TlbCombinator<Account>() {
    val none = AccountNone.tlbConstructor()
    val info = AccountInfo.tlbConstructor()

    override val constructors: List<TlbConstructor<out Account>> =
        listOf(none, info)

    override fun getConstructor(
        value: Account
    ): TlbConstructor<out Account> = when (value) {
        is AccountNone -> none
        is AccountInfo -> info
    }
}
