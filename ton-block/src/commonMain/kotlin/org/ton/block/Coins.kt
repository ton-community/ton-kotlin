package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt

@SerialName("nanocoins")
@Serializable
data class Coins(
    val amount: VarUInteger = VarUInteger(0)
) {
    constructor(amount: Int) : this(VarUInteger(amount))
    constructor(amount: Long) : this(VarUInteger(amount))
    constructor(amount: BigInt) : this(VarUInteger(amount))
}
