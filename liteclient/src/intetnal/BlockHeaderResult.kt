package org.ton.lite.client.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.block.message.address.IntAddr
import org.ton.block.org.ton.account.Account
import org.ton.block.transaction.Transaction
import org.ton.kotlin.bitstring.BitString
import org.ton.kotlin.bitstring.toBitString
import org.ton.tlb.CellRef
import kotlin.jvm.JvmName

internal data class BlockHeaderResult(
    val time: Int,
    val lt: Long,
    val stateHash: BitString?
)

public data class FullAccountState(
    public val blockId: TonNodeBlockIdExt,

    public val address: IntAddr,

    public val lastTransactionId: TransactionId?,

    public val account: CellRef<Account?>
)

@Serializable
public data class TransactionId(
    @get:JvmName("hash")
    val hash: BitString,

    @get:JvmName("lt")
    val lt: Long
) {
    public constructor(hash: ByteArray, lt: Long) : this(hash.toBitString(), lt)

    init {
        require(hash.size == 256) { "expected hash.size == 256, actual: ${hash.size}" }
    }
}

@Serializable
public data class TransactionInfo(
    @SerialName("block_id")
    @get:JvmName("blockId")
    val blockId: TonNodeBlockIdExt,

    @get:JvmName("id")
    val id: TransactionId,

    @get:JvmName("transaction")
    val transaction: CellRef<Transaction>
)
