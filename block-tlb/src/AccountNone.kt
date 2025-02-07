package org.ton.block

import kotlinx.serialization.SerialName
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider


@SerialName("account_none")
public object AccountNone : Account, TlbConstructorProvider<AccountNone> by AccountNoneTlbConstructor {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("account_none")

    override fun toString(): String = print().toString()
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
