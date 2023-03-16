package org.ton.adnl.query

import org.ton.tl.ByteString
import kotlin.random.Random

public class AdnlQueryId(
    public val value: ByteString = ByteString.of(*Random.nextBytes(32))
)
