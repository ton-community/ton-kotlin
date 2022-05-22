@file:Suppress("OPT_IN_USAGE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JsonClassDiscriminator("@type")
@Serializable
sealed interface VmStackList : Iterable<VmStackValue> {
    @SerialName("vm_stk_cons")
    @Serializable
    data class Cons(
        val rest: VmStackList,
        val tos: VmStackValue
    ) : VmStackList {
        override fun iterator(): Iterator<VmStackValue> = ListIterator(this)
    }

    @SerialName("vm_stk_nil")
    @Serializable
    object Nil : VmStackList {
        private val iterator = ListIterator(this)
        override fun iterator(): Iterator<VmStackValue> = iterator
    }

    private class ListIterator(
        var vmStackList: VmStackList
    ) : Iterator<VmStackValue> {
        override fun hasNext(): Boolean = vmStackList != Nil

        override fun next(): VmStackValue {
            val list = vmStackList as Cons
            val value = list.tos
            vmStackList = list.rest
            return value
        }
    }
}
