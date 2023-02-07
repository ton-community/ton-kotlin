@file:Suppress("NOTHING_TO_INLINE")

package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.BigInt
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbCodec
import org.ton.tlb.TlbConstructor
import org.ton.tlb.loadTlb
import org.ton.tlb.storeTlb
import kotlin.jvm.JvmStatic

inline fun VmStack(depth: Int, stack: VmStackList): VmStack = VmStackImpl(depth, stack)
inline fun VmStack(stack: VmStackList): VmStack = VmStackImpl(stack)
inline fun VmStack(): VmStack = VmStackImpl(VmStackList.of())

interface VmStack : Collection<VmStackValue> {
    val depth: Int
    val stack: VmStackList

    override fun iterator(): Iterator<VmStackValue> = stack.iterator()
    override fun contains(element: VmStackValue): Boolean = stack.contains(element)
    override fun containsAll(elements: Collection<VmStackValue>): Boolean = elements.all {
        stack.contains(it)
    }

    override val size: Int get() = depth
    override fun isEmpty(): Boolean = depth <= 0

    fun toMutableVmStack(): MutableVmStack

    operator fun get(index: Int): VmStackValue {
        forEachIndexed { currentIndex, vmStackValue ->
            if (currentIndex == index) {
                return vmStackValue
            }
        }
        throw IllegalArgumentException("index: $index")
    }

    companion object : TlbCodec<VmStack> by VmStackTlbConstructor {
        @JvmStatic
        fun tlbCodec(): TlbConstructor<VmStack> = VmStackTlbConstructor
    }
}

interface MutableVmStack : VmStack {
    fun pop(index: Int = 0): VmStackValue
    fun popNull() = pop() as VmStackNull
    fun popTinyInt() = popNumber().toLong()
    fun popBool() = popTinyInt() != 0L
    fun popInt() = popNumber().toBigInt()
    fun popNumber() = (pop() as VmStackNumber)
    fun popCell() = (pop() as VmStackCell).cell
    fun popSlice() = (pop() as VmStackSlice).toCellSlice()
    fun popBuilder() = (pop() as VmStackBuilder).toCellBuilder()
    fun popCont() = (pop() as VmStackCont).cont
    fun popTuple() = (pop() as VmStackTuple).data

    fun push(stackValue: VmStackValue)
    fun pushNull() = push(VmStackNull)
    fun pushNan() = push(VmStackNan)
    fun pushTinyInt(tinyInt: Byte) = push(VmStackValue(tinyInt.toInt()))
    fun pushTinyInt(tinyInt: UByte) = push(VmStackValue(tinyInt.toInt()))
    fun pushTinyInt(tinyInt: Short) = push(VmStackValue(tinyInt.toInt()))
    fun pushTinyInt(tinyInt: UShort) = push(VmStackValue(tinyInt.toInt()))
    fun pushTinyInt(tinyInt: Int) = push(VmStackValue(tinyInt))
    fun pushTinyInt(tinyInt: UInt) = push(VmStackValue(tinyInt.toInt()))
    fun pushTinyInt(tinyInt: Long) = push(VmStackValue(tinyInt))
    fun pushTinyInt(tinyInt: ULong) = push(VmStackValue(tinyInt.toLong()))
    fun pushTinyInt(tinyInt: Boolean) = push(VmStackValue(tinyInt))
    fun pushBool(boolean: Boolean) = push(VmStackValue(boolean))
    fun pushInt(int: BigInt) = push(VmStackValue(int))
    fun pushCell(cell: Cell) = push(VmStackValue(cell))
    fun pushSlice(cellSlice: CellSlice) = push(VmStackValue(cellSlice))
    fun pushBuilder(cellBuilder: CellBuilder) = push(VmStackValue(cellBuilder))
    fun pushCont(vmCont: VmCont) = push(VmStackValue(vmCont))
    fun pushTuple(vmTuple: VmTuple) = push(VmStackValue(vmTuple))

    fun interchange(i: UByte, j: UByte) = interchange(i.toInt(), j.toInt())
    fun interchange(i: Int, j: Int)
    fun interchange(i: UByte) = interchange(i.toInt())
    fun interchange(i: Int) = interchange(0, i)
    fun swap() = interchange(0, 1)
}

@SerialName("vm_stack")
@Serializable
data class VmStackImpl(
    override val depth: Int,
    override val stack: VmStackList
) : VmStack {
    constructor(stack: VmStackList) : this(stack.count(), stack)

    override fun toMutableVmStack(): MutableVmStack = MutableVmStackImpl(stack)

    override fun toString(): String = "(vm_stack depth:$depth stack:$stack)"
}

inline fun MutableVmStack(): MutableVmStack = MutableVmStackImpl()

class MutableVmStackImpl(
    iterable: Iterable<VmStackValue> = emptyList()
) : MutableVmStack {
    private val _stack = ArrayDeque<VmStackValue>().also { it.addAll(iterable) }
    override val depth: Int get() = _stack.size

    override val stack: VmStackList get() = VmStackList(_stack)
    override fun get(index: Int): VmStackValue = _stack[index]

    override fun pop(index: Int): VmStackValue {
        return _stack.removeAt(index)
    }

    override fun push(stackValue: VmStackValue) {
        _stack.addFirst(stackValue)
    }

    override fun toMutableVmStack(): MutableVmStack = this

    override fun interchange(i: Int, j: Int) {
        val iStackValue = _stack[i]
        val jStackValue = _stack[j]
        _stack[i] = jStackValue
        _stack[j] = iStackValue
    }

    override fun toString(): String = "(vm_stack depth:$depth stack:$stack)"
}

private object VmStackTlbConstructor : TlbConstructor<VmStack>(
    schema = "vm_stack#_ depth:(## 24) stack:(VmStackList depth) = VmStack;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStack
    ) = cellBuilder {
        storeUInt(value.depth, 24)
        storeTlb(VmStackList.tlbCodec(value.depth), value.stack)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStack = cellSlice {
        val depth = loadUInt(24).toInt()
        val stack = loadTlb(VmStackList.tlbCodec(depth))
        VmStackImpl(depth, stack)
    }
}
