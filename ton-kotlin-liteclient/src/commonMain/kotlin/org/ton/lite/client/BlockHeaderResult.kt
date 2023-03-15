package org.ton.lite.client

import org.ton.api.tonnode.TonNodeBlockIdExt
import org.ton.bitstring.Bits256
import org.ton.block.AccountInfo
import org.ton.block.Transaction

public data class BlockHeaderResult(
    val time: Int,
    val lt: Long,
    val stateHash: Bits256?
)

public data class AccountState(
    val info: AccountInfo,
    val lastTransactionHash: Bits256,
    val lastTransactionLt: Long
)

public data class TransactionInfo(
    val blockId: TonNodeBlockIdExt,
    val hash: Bits256,
    val transaction: Transaction
)
