package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("vm_ctl_data")
@Serializable
data class VmControlData(
    val nargs: Maybe<Int>,
    val stack: Maybe<VmStack>
) {
    constructor(nargs: Int?, stack: VmStack?) : this(nargs.toMaybe(), stack.toMaybe())
}
