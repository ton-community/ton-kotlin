@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmTuple {
    @SerialName("vm_tuple_nil")
    object Nil : VmTuple

    @SerialName("vm_tuple_tcons")
    @Serializable
    data class TCons(
        val head: VmTupleRef,
        val tail: VmStackValue
    ) : VmTuple
}
