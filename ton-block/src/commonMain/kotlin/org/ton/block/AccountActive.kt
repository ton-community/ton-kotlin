package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("account_active")
data class AccountActive(
    @SerialName("_")
    val init: StateInit
) : AccountState {
    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AccountActive> = AccountActiveTlbConstructor
    }
}

private object AccountActiveTlbConstructor : TlbConstructor<AccountActive>(
    schema = "account_active\$1 _:StateInit = AccountState;"
) {
    val stateInit by lazy {
        StateInit.tlbCodec()
    }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountActive
    ) = cellBuilder {
        storeTlb(stateInit, value.init)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountActive = cellSlice {
        val init = loadTlb(stateInit)
        AccountActive(init)
    }
}