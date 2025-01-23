package org.ton.block.transaction.phases

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.TlbStorer

@Serializable
public enum class AccountStatusChange {
    @SerialName("acst_unchanged")
    UNCHANGED {
        override fun toString(): String = "acst_unchanged"
    }, // x -> x

    @SerialName("acst_frozen")
    FROZEN {
        override fun toString(): String = "acst_frozen"
    }, // init -> frozen

    @SerialName("acst_deleted")
    DELETED {
        override fun toString(): String = "acst_deleted"
    } // frozen -> deleted
    ;

    public companion object : TlbCodec<AccountStatusChange> by AccStatusChangeTlbCombinator
}

private object AccStatusChangeTlbCombinator : TlbCombinator<AccountStatusChange>(
    AccountStatusChange::class,
    AccountStatusChange::class to AccStatusChangeUnchangedTlbConstructor,
    AccountStatusChange::class to AccStatusChangeFrozenTlbConstructor,
    AccountStatusChange::class to AccStatusChangeDeletedTlbConstructor,
) {
    override fun findTlbStorerOrNull(value: AccountStatusChange): TlbStorer<AccountStatusChange>? {
        return when (value) {
            AccountStatusChange.UNCHANGED -> AccStatusChangeUnchangedTlbConstructor
            AccountStatusChange.FROZEN -> AccStatusChangeFrozenTlbConstructor
            AccountStatusChange.DELETED -> AccStatusChangeDeletedTlbConstructor
        }
    }
}

private object AccStatusChangeUnchangedTlbConstructor : TlbConstructor<AccountStatusChange>(
    schema = "acst_unchanged\$0 = AccStatusChange;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatusChange) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatusChange = AccountStatusChange.UNCHANGED
}

private object AccStatusChangeFrozenTlbConstructor : TlbConstructor<AccountStatusChange>(
    schema = "acst_frozen\$10 = AccStatusChange;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatusChange) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatusChange = AccountStatusChange.FROZEN
}

private object AccStatusChangeDeletedTlbConstructor : TlbConstructor<AccountStatusChange>(
    schema = "acst_deleted\$11 = AccStatusChange;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: AccountStatusChange) {
    }

    override fun loadTlb(cellSlice: CellSlice): AccountStatusChange = AccountStatusChange.DELETED
}
