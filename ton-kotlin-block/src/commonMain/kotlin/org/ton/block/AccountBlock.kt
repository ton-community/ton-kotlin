package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.Bits256
import org.ton.cell.*
import org.ton.hashmap.AugDictionaryEdge
import org.ton.tlb.*
import org.ton.tlb.providers.TlbCombinatorProvider

@Serializable
@SerialName("acc_trans")
public data class AccountBlock(
    @SerialName("account_addr") val accountAddr: Bits256,
    val transactions: AugDictionaryEdge<CellRef<Transaction>, CurrencyCollection>,
    @SerialName("state_update") val stateUpdate: CellRef<HashUpdate>
) : TlbObject {
    override fun print(printer: TlbPrettyPrinter): TlbPrettyPrinter = printer {
        type("acc_trans") {
            field("account_addr", accountAddr)
            field("transactions", transactions)
            field("state_update", stateUpdate)
        }
    }

    override fun toString(): String = print().toString()

    public companion object : TlbCombinatorProvider<AccountBlock> by AccountBlockTlbCombinator
}

private object AccountBlockTlbCombinator : TlbCombinator<AccountBlock>(
    AccountBlock::class,
    AccountBlock::class to AccountBlockTlbConstructor
)

private object AccountBlockTlbConstructor : TlbConstructor<AccountBlock>(
    schema = "acc_trans#5 account_addr:bits256 " +
        "transactions:(AugDictionaryEdge 64 ^Transaction CurrencyCollection) " +
        "state_update:^(HASH_UPDATE Account) " +
        "= AccountBlock;"
) {
    val augDictionaryEdge = AugDictionaryEdge.tlbCodec(
        64,
        CellRef.tlbCodec(Transaction),
        CurrencyCollection
    )

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountBlock
    ) = cellBuilder {
        storeBits(value.accountAddr)
        storeTlb(augDictionaryEdge, value.transactions)
        storeRef(HashUpdate, value.stateUpdate)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountBlock = cellSlice {
        val accountAddr = loadBits256()
        val transactions = loadTlb(augDictionaryEdge)
        val stateUpdate = loadRef(HashUpdate)
        AccountBlock(accountAddr, transactions, stateUpdate)
    }
}
