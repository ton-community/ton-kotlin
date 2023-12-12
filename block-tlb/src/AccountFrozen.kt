package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbPrettyPrinter
import org.ton.tlb.providers.TlbConstructorProvider
import kotlin.jvm.JvmName

@Serializable
@SerialName("account_frozen")
public data class AccountFrozen(
    @SerialName("state_hash")
    @get:JvmName("stateHash")
    val stateHash: BitString // state_hash : bits256
) : AccountState {
    init {
        require(stateHash.size == 256) { "stateHash must be 256 bits long" }
    }

    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer.type("account_frozen") {
        printer.field("state_hash", stateHash)
    }

    override fun toString(): String = print().toString()

    public companion object : TlbConstructorProvider<AccountFrozen> by AccountFrozenTlbConstructor
}

private object AccountFrozenTlbConstructor : TlbConstructor<AccountFrozen>(
    schema = "account_frozen\$01 state_hash:bits256 = AccountState;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountFrozen
    ) = cellBuilder {
        storeBits(value.stateHash)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountFrozen = cellSlice {
        val stateHash = loadBits(256)
        AccountFrozen(stateHash)
    }
}
