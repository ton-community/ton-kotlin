package org.ton.contract.wallet

import io.ktor.utils.io.bits.*
import org.ton.api.pub.PublicKeyEd25519
import org.ton.bitstring.BitString
import org.ton.bitstring.Bits256
import org.ton.block.AccountState
import org.ton.block.MsgAddressInt
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.buildCell
import org.ton.hashmap.HashMapE
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.constructor.AnyTlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

public class HighLoadWalletV2Contract(
    override val address: MsgAddressInt,
    public override val state: AccountState
) : WalletContract<HighLoadWalletV2Contract.Data> {
    override fun loadData(): Data? = data?.let {
        Data.loadTlb(it)
    }

    public fun getSubWalletId(): Int = requireNotNull(loadData()).subWalletId

    public fun getLastCleaned(): Long = requireNotNull(loadData()).lastCleaned

    public fun getPublicKey(): PublicKeyEd25519 = PublicKeyEd25519(requireNotNull(loadData()).publicKey)

    public class Data(
        public val subWalletId: Int,
        public val lastCleaned: Long,
        public val publicKey: Bits256,
        public val oldQueries: HashMapE<WalletMessage<Cell>>
    ) {
        public companion object : TlbConstructorProvider<Data> by HighLoadWalletV2DataTlbConstructor
    }

    public class Query<X : Any>(
        public val signature: BitString,
        public val payload: QueryPayload<X>
    ) {
        public companion object {
            @JvmStatic
            public fun <X : Any> tlbConstructor(x: TlbCodec<X>): TlbConstructor<Query<X>> =
                HighLoadWalletV2QueryTlbConstructor(x)
        }
    }

    public data class QueryPayload<X : Any>(
        public val subWalletId: Int,
        public val queryId: Long,
        public val msgs: HashMapE<WalletMessage<X>>
    ) {
        public companion object {
            @JvmStatic
            public fun <X : Any> of(
                subWalletId: Int,
                queryId: Long,
                msgs: HashMapE<WalletMessage<X>>
            ): QueryPayload<X> = QueryPayload(subWalletId, queryId, msgs)

            @JvmStatic
            public fun <X : Any> of(
                subWalletId: Int,
                x: TlbCodec<X>,
                msgs: HashMapE<WalletMessage<X>>,
            ): QueryPayload<X> {
                val codec = HashMapE.tlbCodec(16, WalletMessage.tlbCodec(x))
                val queryId = buildCell {
                    storeTlb(codec, msgs)
                }.hash().useMemory(0, 64) {
                    it.loadLongAt(0)
                }
                return of(subWalletId, queryId, msgs)
            }

            @JvmStatic
            public fun <X : Any> of(
                subWalletId: Int,
                x: TlbCodec<X>,
                msgs: Iterable<WalletMessage<X>>
            ): QueryPayload<X> {
                var hashMap = HashMapE.empty<WalletMessage<X>>()
                msgs.forEachIndexed { index, walletMessage ->
                    val key = buildCell {
                        storeInt(index, 16)
                    }.bits
                    hashMap = hashMap.set(key, walletMessage)
                }
                return of(subWalletId, x, hashMap)
            }

            @JvmStatic
            public fun <X:Any> tlbConstructor(x: TlbCodec<X>): TlbConstructor<QueryPayload<X>> =
                HighLoadWalletV2QueryPayloadTlbConstructor(x)
        }
    }
}

private class HighLoadWalletV2QueryTlbConstructor<X : Any>(
    val x: TlbCodec<X>
) : TlbConstructor<HighLoadWalletV2Contract.Query<X>>(
    schema = "highload_wallet_v2_query#_ signature:(bits 512) payload:(HighLoadWalletV2QueryPayload X) = HighLoadWalletV2Query X"
) {
    override fun loadTlb(cellSlice: CellSlice): HighLoadWalletV2Contract.Query<X> {
        val signature = cellSlice.loadBits(512)
        val payload = cellSlice.loadTlb(HighLoadWalletV2Contract.QueryPayload.tlbConstructor(x))
        return HighLoadWalletV2Contract.Query(signature, payload)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HighLoadWalletV2Contract.Query<X>) {
        cellBuilder.storeBits(value.signature)
        HighLoadWalletV2Contract.QueryPayload.tlbConstructor(x).storeTlb(cellBuilder, value.payload)
    }
}

private class HighLoadWalletV2QueryPayloadTlbConstructor<X : Any>(
    x: TlbCodec<X>
) : TlbConstructor<HighLoadWalletV2Contract.QueryPayload<X>>(
    schema = "highload_wallet_v2_query_payload#_ subwallet_id:uint32 query_id:uint64 msgs:(HashmapE 16 WalletMessage X) = HighLoadWalletV2QueryPayload X"
) {
    val msgs = HashMapE.tlbCodec(16, WalletMessage.tlbCodec(x))

    override fun loadTlb(cellSlice: CellSlice): HighLoadWalletV2Contract.QueryPayload<X> {
        val subWalletId = cellSlice.loadInt(32).toInt()
        val queryId = cellSlice.loadInt(64).toLong()
        val msgs = cellSlice.loadTlb(msgs)
        return HighLoadWalletV2Contract.QueryPayload(subWalletId, queryId, msgs)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HighLoadWalletV2Contract.QueryPayload<X>) {
        cellBuilder.storeInt(value.subWalletId, 32)
        cellBuilder.storeInt(value.queryId, 64)
        cellBuilder.storeTlb(msgs, value.msgs)
    }
}

private object HighLoadWalletV2DataTlbConstructor : TlbConstructor<HighLoadWalletV2Contract.Data>(
    schema = "highload_wallet_v2_data#_ sub_wallet_id:uint32 last_cleaned:uint64 public_key:bits256 old_queries:HighLoadWalletV2Queries = HighLoadWalletV2Data"
) {
    override fun loadTlb(cellSlice: CellSlice): HighLoadWalletV2Contract.Data {
        val subWalletId = cellSlice.loadInt(32).toInt()
        val lastCleaned = cellSlice.loadInt(64).toLong()
        val publicKey = cellSlice.loadBits256()
        val oldQueries = cellSlice.loadTlb(HighLoadWalletV2QueriesTlbConstructor)
        return HighLoadWalletV2Contract.Data(subWalletId, lastCleaned, publicKey, oldQueries)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HighLoadWalletV2Contract.Data) {
        cellBuilder.storeInt(value.subWalletId, 32)
        cellBuilder.storeInt(value.lastCleaned, 64)
        cellBuilder.storeBits(value.publicKey)
        cellBuilder.storeTlb(HighLoadWalletV2QueriesTlbConstructor, value.oldQueries)
    }
}

private object HighLoadWalletV2QueriesTlbConstructor : TlbConstructor<HashMapE<WalletMessage<Cell>>>(
    schema = "_ (HashmapE 64 WalletMessage Any) = HighLoadWalletV2Queries"
) {
    val hashMapEHighLoadWalletV2QueriesEntry = HashMapE.tlbCodec(64, WalletMessage.tlbCodec(AnyTlbConstructor))

    override fun loadTlb(cellSlice: CellSlice): HashMapE<WalletMessage<Cell>> {
        return cellSlice.loadTlb(hashMapEHighLoadWalletV2QueriesEntry)
    }

    override fun storeTlb(cellBuilder: CellBuilder, value: HashMapE<WalletMessage<Cell>>) {
        cellBuilder.storeTlb(hashMapEHighLoadWalletV2QueriesEntry, value)
    }
}
