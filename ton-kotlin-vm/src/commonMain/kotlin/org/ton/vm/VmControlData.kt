package org.ton.vm

import org.ton.block.Maybe
import org.ton.vm.stack.VmStack

/**
 * ```tl-b
 * vm_ctl_data$_ nargs:(Maybe uint13) stack:(Maybe VmStack) save:VmSaveList
 * cp:(Maybe int16) = VmControlData;
 */
public data class VmControlData(
    /**
     * Indicating the number of arguments expected by the continuation.
     */
    val nargs: Maybe<Int>,
    /**
     * Containing the original contents of the stack for the code to be executed.
     */
    val stack: Maybe<VmStack>,
    /**
     * List of pairs (`c(i)`, `vi`) (also called “savelist”),
     * containing the values of control registers to be restored before the execution of the code.
     */
    val save: VmSaveList,
    /**
     * Selecting the TVM codepage used to interpret the TVM code from code.
     */
    val cp: Maybe<Int>
)
