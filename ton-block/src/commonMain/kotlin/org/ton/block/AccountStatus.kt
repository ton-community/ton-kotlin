package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor

@Serializable
enum class AccountStatus {
    @SerialName("acc_state_uninit")
    UNINIT,

    @SerialName("acc_state_frozen")
    FROZEN,

    @SerialName("acc_state_active")
    ACTIVE,

    @SerialName("acc_state_nonexist")
    NONEXIST;

    companion object {
        @JvmStatic
        fun tlbCodec(): TlbCombinator<AccountStatus> = AccountStatusTlbCombinator
    }
}

private object AccountStatusTlbCombinator : TlbCombinator<AccountStatus>() {
    override val constructors: List<TlbConstructor<out AccountStatus>> =
        listOf(
            AccountStatusUninitTlbConstructor,
            AccountStatusFrozenTlbConstructor,
            AccountStatusActiveTlbConstructor,
            AccountStatusNonExistTlbConstructor
        )

    override fun getConstructor(
        value: AccountStatus
    ): TlbConstructor<out AccountStatus> = when (value) {
        AccountStatus.UNINIT -> AccountStatusUninitTlbConstructor
        AccountStatus.FROZEN -> AccountStatusFrozenTlbConstructor
        AccountStatus.ACTIVE -> AccountStatusActiveTlbConstructor
        AccountStatus.NONEXIST -> AccountStatusNonExistTlbConstructor
    }
}

private object AccountStatusUninitTlbConstructor : TlbConstructor<AccountStatus>(
    schema = "acc_state_uninit\$00 = AccountStatus;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatus) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatus = AccountStatus.UNINIT
}

private object AccountStatusFrozenTlbConstructor : TlbConstructor<AccountStatus>(
    schema = "acc_state_frozen\$01 = AccountStatus;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatus) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatus = AccountStatus.FROZEN
}

private object AccountStatusActiveTlbConstructor : TlbConstructor<AccountStatus>(
    schema = "acc_state_active\$10 = AccountStatus;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatus) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatus = AccountStatus.ACTIVE
}

private object AccountStatusNonExistTlbConstructor : TlbConstructor<AccountStatus>(
    schema = "acc_state_nonexist\$11 = AccountStatus;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatus) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatus = AccountStatus.NONEXIST
}