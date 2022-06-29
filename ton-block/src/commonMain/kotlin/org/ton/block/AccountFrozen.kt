package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@Serializable
@SerialName("account_frozen")
data class AccountFrozen(
    val state_hash: BitString
) : AccountState {
    init {
        require(state_hash.size == 256) { "required: state_hash.size == 256, actual: ${state_hash.size}" }
    }

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<AccountFrozen> = AccountFrozenTlbConstructor
    }
}

private object AccountFrozenTlbConstructor : TlbConstructor<AccountFrozen>(
    schema = "account_frozen\$01 state_hash:bits256 = AccountState"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountFrozen
    ) = cellBuilder {
        storeBits(value.state_hash)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountFrozen = cellSlice {
        val stateHash = loadBits(256)
        AccountFrozen(stateHash)
    }
}