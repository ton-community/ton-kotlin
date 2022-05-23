package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("vm_ctl_data")
@Serializable
data class VmControlData(
    val nargs: Maybe<Int>,
    val stack: Maybe<VmStack>,
    val save: VmSaveList,
    val cp: Maybe<Int>
) {
    constructor(nargs: Int?, stack: VmStack?, save: VmSaveList, cp: Int?) : this(
        nargs.toMaybe(),
        stack.toMaybe(),
        save,
        cp.toMaybe()
    )
}
