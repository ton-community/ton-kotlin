package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("vm_stack")
@Serializable
data class VmStack(
    val depth: Int,
    val stack: VmStackList
)
