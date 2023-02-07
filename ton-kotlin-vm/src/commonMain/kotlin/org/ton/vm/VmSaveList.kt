package org.ton.vm

import org.ton.bitstring.BitString
import org.ton.hashmap.HashMapE
import org.ton.vm.VmStackValue.*
import kotlin.jvm.JvmInline

/**
 * ```tl-b
 * _ cregs:(HashmapE 4 VmStackValue) = VmSaveList;
 */
@JvmInline
public value class VmSaveList(
    public val cregs: HashMapE<VmStackValue>
) {
    public val c0: VmStkCont? get() = cont(0)
    public val c1: VmStkCont? get() = cont(1)
    public val c2: VmStkCont? get() = cont(2)
    public val c3: VmStkCont? get() = cont(3)
    public val c4: VmStkCell? get() = data(4)
    public val c5: VmStkCell? get() = data(5)
    public val c7: VmStkTuple?
        get() = cregs[key(7)]?.let { it as VmStkTuple }

    public fun cont(index: Int): VmStkCont? = cont(key(index))
    public fun cont(key: BitString): VmStkCont? = cregs[key]?.let { it as? VmStkCont }

    public fun data(index: Int): VmStkCell? = data(key(index))
    public fun data(key: BitString): VmStkCell? = cregs[key]?.let { it as? VmStkCell }

    public fun cont(index: Int, value: VmStkCont?): VmSaveList = cont(key(index), value)
    public fun cont(key: BitString, value: VmStkCont?): VmSaveList {
        return if (value != null) VmSaveList(cregs.set(key, value))
        else TODO()
    }

    public fun data(index: Int, value: VmStkCell?): VmSaveList = data(key(index), value)
    public fun data(key: BitString, value: VmStkCell?): VmSaveList {
        return if (value != null) VmSaveList(cregs.set(key, value))
        else TODO()
    }

    public fun apply(other: VmSaveList): VmSaveList {
        if (other.cregs.isEmpty()) return this
        var result = cregs
        for ((key, value) in other.cregs.nodes()) {
            result = cregs.set(key, value)
        }
        return VmSaveList(result)
    }
}

private fun key(index: Int): BitString = when(index) {
    0 -> BitString.binary("0000")
    1 -> BitString.binary("0001")
    2 -> BitString.binary("0010")
    3 -> BitString.binary("0011")
    4 -> BitString.binary("0100")
    5 -> BitString.binary("0101")
    6 -> BitString.binary("0110")
    7 -> BitString.binary("0111")
    else -> throw IndexOutOfBoundsException()
}
