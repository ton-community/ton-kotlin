package org.ton.adnl.query

import org.ton.bitstring.Bits256
import kotlin.jvm.JvmInline
import kotlin.random.Random

@JvmInline
public value class AdnlQueryId(
    public val value: Bits256 = Bits256(Random.nextBytes(32))
)
