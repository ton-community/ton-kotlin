package org.ton.tonlib.types

internal data class RWalletConfig(
    val startAt: Long,
    val limits: List<RWalletLimit>
)
