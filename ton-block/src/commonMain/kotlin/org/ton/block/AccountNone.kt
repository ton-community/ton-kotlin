package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("account_none")
object AccountNone : Account {
    @JvmStatic
    fun tlbCodec(): TlbConstructor<AccountNone> = AccountNoneTlbConstructor
}

private object AccountNoneTlbConstructor : TlbConstructor<AccountNone>(
    schema = "account_none\$0 = Account;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountNone) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountNone {
        return AccountNone
    }
}