@file:Suppress("OPT_IN_USAGE")

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

    companion object {
        @JvmStatic
        fun of(): VmTupleRef = VmTupleRef()

        @JvmStatic
        fun of(entry: VmStackValue): VmTupleRef = VmTupleRef(entry)

        @JvmStatic
        fun of(ref: VmTuple): VmTupleRef = VmTupleRef(ref)
    }
}

fun VmTupleRef(): VmTupleRef = VmTupleRef.Nil
fun VmTupleRef(entry: VmStackValue): VmTupleRef = VmTupleRef.Single(entry)
fun VmTupleRef(ref: VmTuple): VmTupleRef = VmTupleRef.Any(ref)
