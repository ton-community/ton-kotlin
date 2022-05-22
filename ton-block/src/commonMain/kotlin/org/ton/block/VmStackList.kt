@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmStackList {
    @SerialName("vm_stk_cons")
    @Serializable
    data class Cons(
        val rest: VmStackList,
        val tos: VmStackValue
    )

    @SerialName("vm_stk_nil")
    @Serializable
    object Nil : VmStackList
}
