package org.ton.vm

data class VmGas(
    var gasLimit: Long = 0L,
    var gasMax: Long = 0L,
    var gasRemaining: Long = 0L,
    var gasCredit: Long = 0L
)