package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bitstring.BitString
import org.ton.cell.*
import org.ton.hashmap.AugDictionaryEdge
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.tlbCodec
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

@Serializable
@SerialName("acc_trans")
data class AccountBlock(
    val account_addr: BitString,
    val transactions: AugDictionaryEdge<Transaction, CurrencyCollection>,
    val state_update: HashUpdate<Account>
) {
    init {
        require(account_addr.size == 256) { "expected: account_addr.size == 256, actual: ${account_addr.size}" }
    }

    companion object : TlbCodec<AccountBlock> by AccountBlockTlbConstructor.asTlbCombinator()
}

private object AccountBlockTlbConstructor : TlbConstructor<AccountBlock>(
    schema = "acc_trans#5 account_addr:bits256 " +
            "transactions:(AugDictionaryEdge 64 ^Transaction CurrencyCollection) " +
            "state_update:^(HASH_UPDATE Account) " +
            "= AccountBlock;"
) {
    val augDictionaryEdge by lazy {
        AugDictionaryEdge.tlbCodec(
            64,
            Cell.tlbCodec(Transaction),
            CurrencyCollection.tlbCodec()
        )
    }
    val hashUpdate by lazy { HashUpdate.tlbCodec(Account.tlbCodec()) }

    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: AccountBlock
    ) = cellBuilder {
        storeBits(value.account_addr)
        storeTlb(augDictionaryEdge, value.transactions)
        storeRef {
            storeTlb(hashUpdate, value.state_update)
        }
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): AccountBlock = cellSlice {
        val accountAddr = loadBitString(256)
        val transactions = loadTlb(augDictionaryEdge)
        val stateUpdate = loadRef {
            loadTlb(hashUpdate)
        }
        AccountBlock(accountAddr, transactions, stateUpdate)
    }
}