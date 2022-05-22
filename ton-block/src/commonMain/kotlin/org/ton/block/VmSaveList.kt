package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.hashmap.HashMapE

@SerialName("_")
@Serializable
data class VmSaveList(
    val cregs: HashMapE<VmStackValue>
)
