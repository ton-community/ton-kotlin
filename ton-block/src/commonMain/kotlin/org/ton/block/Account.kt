@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@JsonClassDiscriminator("@type")
@Serializable
sealed interface Account {
    companion object : TlbCodec<Account> by AccountTlbCombinator {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<Account> = AccountTlbCombinator
    }
}

private object AccountTlbCombinator : TlbCombinator<Account>() {
    val none = AccountNone.tlbCodec()
    val info = AccountInfo.tlbCodec()

    override val constructors: List<TlbConstructor<out Account>> =
        listOf(none, info)

    override fun getConstructor(
        value: Account
    ): TlbConstructor<out Account> = when (value) {
        is AccountNone -> none
        is AccountInfo -> info
    }
}
