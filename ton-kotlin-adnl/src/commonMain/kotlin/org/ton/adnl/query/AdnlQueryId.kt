package org.ton.adnl.query

import org.ton.bitstring.BitString
import kotlin.jvm.JvmInline
import kotlin.random.Random

@JvmInline
value class AdnlQueryId(
    val value: BitString = BitString(Random.nextBytes(32))
)
