package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmTupleRef {
    @SerialName("vm_tupref_nil")
    @Serializable
    object Nil : VmTupleRef

    @SerialName("vm_tupref_single")
    @Serializable
    data class Single(
        val entry: VmStackValue
    ) : VmTupleRef

    @SerialName("vm_tupref_any")
    @Serializable
    data class Any(
        val ref: VmTuple
    ) : VmTupleRef
}
