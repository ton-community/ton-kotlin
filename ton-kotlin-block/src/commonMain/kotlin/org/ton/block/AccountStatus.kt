package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbLoader
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
public enum class AccountStatus {
    @SerialName("acc_state_uninit")
    UNINIT {
        override fun toString(): String = "acc_state_uninit"
    },

    @SerialName("acc_state_frozen")
    FROZEN {
        override fun toString(): String = "acc_state_frozen"
    },

    @SerialName("acc_state_active")
    ACTIVE {
        override fun toString(): String = "acc_state_active"
    },

    @SerialName("acc_state_nonexist")
    NONEXIST {
        override fun toString(): String = "acc_state_nonexist"
    };

    public companion object : TlbCombinatorProvider<AccountStatus> by AccountStatusTlbCombinator
}

private object AccountStatusTlbCombinator : TlbCombinator<AccountStatus>(
    AccountStatus::class,
    AccountStatus::class to AccountStatusUninitTlbConstructor,
    AccountStatus::class to AccountStatusFrozenTlbConstructor,
    AccountStatus::class to AccountStatusActiveTlbConstructor,
    AccountStatus::class to AccountStatusNonExistTlbConstructor,
) {
    override fun findTlbStorerOrNull(value: AccountStatus): TlbConstructor<AccountStatus>? {
        return when (value) {
            AccountStatus.UNINIT -> AccountStatusUninitTlbConstructor
            AccountStatus.FROZEN -> AccountStatusFrozenTlbConstructor
            AccountStatus.ACTIVE -> AccountStatusActiveTlbConstructor
            AccountStatus.NONEXIST -> AccountStatusNonExistTlbConstructor
        }
    }

    override fun findTlbLoaderOrNull(bitString: BitString): TlbLoader<out AccountStatus>? {
        return if (bitString.size >= 2) {
            if (bitString[0]) { // 1
                if (bitString[1]) { // 11
                    AccountStatusNonExistTlbConstructor
                } else { // 10
                    AccountStatusActiveTlbConstructor
                }
            } else { // 0
                if (bitString[1]) { // 01
                    AccountStatusFrozenTlbConstructor
                } else { // 00
                    AccountStatusUninitTlbConstructor
                }
            }
        } else {
            null
        }
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
