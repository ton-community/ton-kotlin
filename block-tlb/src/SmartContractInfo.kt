package org.ton.block

import org.ton.bitstring.BitString
import org.ton.block.currency.CurrencyCollection
import org.ton.block.message.address.AddrInt

public data class SmartContractInfo(
    val actions: Int,
    val msgs_sent: Int,
    val unixtime: Long,
    val block_lt: Long,
    val trans_lt: Long,
    val rand_seed: BitString,
    val balance_remaining: CurrencyCollection,
    val myself: AddrInt
) {
    init {
        require(rand_seed.size == 256) { "required: rand_seed.size == 256, actual: ${rand_seed.size}" }
    }
}
