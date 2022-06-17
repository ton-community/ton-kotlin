@file:Suppress("OPT_IN_USAGE", "NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb

inline fun VmStackList(vararg stackValues: VmStackValue): VmStackList = VmStackList.of(*stackValues)
inline fun VmStackList(stackValues: Iterable<VmStackValue>): VmStackList = VmStackList.of(stackValues)

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

    companion object {
        @JvmStatic
        fun of(vararg stackValues: VmStackValue) = of(stackValues.toList())

        @JvmStatic
        fun of(stackValues: Iterable<VmStackValue>): VmStackList {
            var stackList: VmStackList = Nil
            stackValues.forEach { value ->
                stackList = Cons(stackList, value)
            }
            return stackList
        }

        @JvmStatic
        fun tlbCodec(n: Int): TlbCombinator<VmStackList> = VmStackListCombinator(n)
    }
}

private class VmStackListCombinator(val n: Int) : TlbCombinator<VmStackList>() {
    private val vmStkConsConstructor by lazy {
        VmStackListConsConstructor(n)
    }
    private val vmStkNilConstructor by lazy {
        VmStackListNilConstructor()
    }

    override val constructors: List<TlbConstructor<out VmStackList>> by lazy {
        listOf(vmStkConsConstructor, vmStkNilConstructor)
    }

    override fun getConstructor(value: VmStackList): TlbConstructor<out VmStackList> = when (value) {
        is VmStackList.Cons -> vmStkConsConstructor
        is VmStackList.Nil -> vmStkNilConstructor
    }

    override fun loadTlb(cellSlice: CellSlice): VmStackList {
        return if (n == 0) {
            vmStkNilConstructor.loadTlb(cellSlice)
        } else {
            vmStkConsConstructor.loadTlb(cellSlice)
        }
    }

    private class VmStackListConsConstructor(
        n: Int
    ) : TlbConstructor<VmStackList.Cons>(
        schema = "vm_stk_cons#_ {n:#} rest:^(VmStackList n) tos:VmStackValue = VmStackList (n + 1);"
    ) {
        private val vmStackListCodec by lazy {
            VmStackList.tlbCodec(n - 1)
        }
        private val vmStackValue by lazy {
            VmStackValue.tlbCodec()
        }

        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackList.Cons
        ) = cellBuilder {
            storeRef {
                storeTlb(vmStackListCodec, value.rest)
            }
            storeTlb(vmStackValue, value.tos)
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackList.Cons = cellSlice {
            val rest = loadRef {
                loadTlb(vmStackListCodec)
            }
            val tos = loadTlb(vmStackValue)
            VmStackList.Cons(rest, tos)
        }
    }

    private class VmStackListNilConstructor : TlbConstructor<VmStackList.Nil>(
        schema = "vm_stk_nil#_ = VmStackList 0;"
    ) {
        override fun storeTlb(
            cellBuilder: CellBuilder,
            value: VmStackList.Nil
        ) {
        }

        override fun loadTlb(
            cellSlice: CellSlice
        ): VmStackList.Nil {
            return VmStackList.Nil
        }
    }
}
