package org.ton.lite.client.internal

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.BitString
import org.ton.bitstring.toBitString
import org.ton.block.Account
import org.ton.block.AddrStd
import org.ton.block.Transaction
import org.ton.tlb.CellRef
import kotlin.jvm.JvmName

internal data class BlockHeaderResult(
    val time: Int,
    val lt: Long,
    val stateHash: BitString?
)

public class FullAccountState(
    @get:JvmName("blockId")
    public val blockId: TonNodeBlockIdExt,

    @get:JvmName("address")
    public val address: AddrStd,

    @get:JvmName("lastTransactionId")
    public val lastTransactionId: TransactionId?,

    @get:JvmName("accountInfo")
    public val account: CellRef<Account>
)

public data class TransactionId(
    @get:JvmName("hash")
    val hash: BitString,

    @get:JvmName("id")
    val lt: Long
) {
    public constructor(hash: ByteArray, lt: Long) : this(hash.toBitString(), lt)

    init {
        require(hash.size == 256) { "expected hash.size == 256, actual: ${hash.size}" }
    }
}

public data class TransactionInfo(
    @get:JvmName("blockId")
    val blockId: TonNodeBlockIdExt,

    @get:JvmName("id")
    val id: TransactionId,

    @get:JvmName("transaction")
    val transaction: CellRef<Transaction>
)
