@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
@JsonClassDiscriminator("@type")
sealed interface AccountState {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<AccountState> = AccountStateTlbCombinator
    }
}

private object AccountStateTlbCombinator : TlbCombinator<AccountState>() {
    val uninit by lazy {
        AccountUninit.tlbCodec()
    }
    val active by lazy {
        AccountActive.tlbCodec()
    }
    val frozen by lazy {
        AccountFrozen.tlbCodec()
    }

    override val constructors: List<TlbConstructor<out AccountState>> by lazy {
        listOf(uninit, active, frozen)
    }

    override fun getConstructor(
        value: AccountState
    ): TlbConstructor<out AccountState> = when (value) {
        is AccountUninit -> uninit
        is AccountActive -> active
        is AccountFrozen -> frozen
    }
}