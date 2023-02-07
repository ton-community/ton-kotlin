package org.ton.vm.stack

import org.ton.bigint.BigInt
import org.ton.tlb.CellRef
import org.ton.vm.VmStackList
import org.ton.vm.VmStackValue
import org.ton.vm.exception.TvmStackUnderflowException
import org.ton.vm.exception.TvmTypeCheckException
import kotlin.jvm.JvmStatic

/**
 * ```tl-b
 * vm_stack#_ depth:(## 24) stack:(VmStackList depth) = VmStack;
 */
public interface VmStack : Iterable<VmStackValue> {
    public val depth: Int
    public val stack: VmStackList

    public operator fun get(index: Int): VmStackValue

    public fun getNumberAt(index: Int): VmStackValue.VmStkNumber =
        get(index) as? VmStackValue.VmStkNumber ?: throw TvmTypeCheckException("Expected VmStkNumber, got ${get(index)}")

    public fun getBigIntAt(index: Int): BigInt = getNumberAt(index).toBigInt()

    public fun getIntAt(index: Int): Int = getNumberAt(index).toInt()

    public fun getLongAt(index: Int): Long = getNumberAt(index).toLong()

    public fun isEmpty(): Boolean = depth == 0

    public fun isNotEmpty(): Boolean = depth != 0

    public fun checkDepth(depth: Int) {
        check(depth <= this.depth) {
            throw TvmStackUnderflowException("Stack underflow: depth=$depth, stack depth=${this.depth}")
        }
    }

    public companion object {
        @JvmStatic
        public fun fromIterable(iterable: Iterable<VmStackValue>): VmStack {
            var depth = 0
            var list: VmStackList = VmStackList.VmStkNil
            for (value in iterable) {
                depth++
                list = VmStackList.VmStkCons(list.n, CellRef(list), value)
            }
            return VmStackImpl(depth, list)
        }
    }
}

public inline fun Iterable<VmStackValue>.toVmStack(): VmStack = VmStack.fromIterable(this)

private data class VmStackImpl(
    override var depth: Int,
    override var stack: VmStackList
) : VmStack {
    override fun get(index: Int): VmStackValue {
        if (index < 0 || index >= depth) {
            throw IndexOutOfBoundsException()
        }
        var i = index
        var list = stack as VmStackList.VmStkCons
        while (i > 0) {
            list = list.loadRes() as VmStackList.VmStkCons
            i--
        }
        return list.tos
    }

    override fun iterator(): Iterator<VmStackValue> {
        return object : Iterator<VmStackValue> {
            var list = stack

            override fun hasNext(): Boolean = list is VmStackList.VmStkCons

            override fun next(): VmStackValue {
                val list = list
                if (list is VmStackList.VmStkCons) {
                    val value = list.tos
                    this.list = list.loadRes()
                    return value
                } else {
                    throw TvmStackUnderflowException()
                }
            }
        }
    }
}
