package org.ton.kotlin.account

import kotlinx.io.bytestring.ByteString
import org.ton.kotlin.cell.CellContext
import org.ton.kotlin.cell.CellRef

/**
 * Shard accounts entry.
 */
public data class ShardAccount(
    /**
     * Optional reference to account state.
     */
    val account: CellRef<Account?>,

    /**
     * The exact hash of the last transaction.
     */
    val lastTransHash: ByteString,

    /**
     * The exact logical time of the last transaction.
     */
    val lastTransLt: ULong
) {
    /**
     * Tries to load account data.
     */
    public fun loadAccount(context: CellContext = CellContext.EMPTY): Account? = account.load(context)

//    public object Tlb : TlbCodec<ShardAccount> {
//        override fun storeTlb(
//            cellBuilder: CellBuilder,
//            value: ShardAccount
//        ): Unit = cellBuilder {
//            storeRef(NullableAccountTlbCodec, value.account)
//            storeByteString(value.lastTransHash)
//            storeUInt64(value.lastTransLt)
//        }
//
//        override fun loadTlb(
//            cellSlice: CellSlice
//        ): ShardAccount = cellSlice {
//            val account = loadRef(Account.Tlb)
//            val lastTransHash = loadByteString(32)
//            val lastTransLt = loadULong()
//            ShardAccount(account, lastTransHash, lastTransLt)
//        }
//
//        private object NullableAccountTlbCodec : TlbCodec<Account?> {
//            override fun storeTlb(cellBuilder: CellBuilder, value: Account?) {
//                if (value == null) {
//                    cellBuilder.storeBoolean(false)
//                } else {
//                    cellBuilder.storeBoolean(true)
//                    cellBuilder.storeTlb(Account.Tlb, value)
//                }
//            }
//
//            override fun loadTlb(cellSlice: CellSlice): Account? {
//                return if (cellSlice.loadBit()) {
//                    cellSlice.loadTlb(Account.Tlb)
//                } else {
//                    null
//                }
//            }
//        }
//    }

    public companion object
}


