package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.bigint.*
import org.ton.block.VmStackNan.VmStackNanException
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.cell.invoke
import org.ton.tlb.TlbConstructor

@SerialName("vm_stk_int")
@Serializable
data class VmStackInt(
    @Serializable(BigIntSerializer::class)
    val value: BigInt
) : VmStackValue, VmStackNumber {
    constructor(byte: Byte) : this(BigInt(byte))
    constructor(short: Short) : this(BigInt(short))
    constructor(int: Int) : this(BigInt(int))
    constructor(long: Long) : this(BigInt(long))

    override fun toInt(): Int = value.toInt()
    override fun toLong(): Long = value.toLong()
    override fun toBigInt(): BigInt = value
    override fun toBoolean(): Boolean = value != BigInt(0)

    override fun plus(other: VmStackNumber): VmStackNumber = when (other) {
        is VmStackInt -> VmStackInt(value + other.value)
        is VmStackTinyInt -> VmStackInt(value + other.value)
        VmStackNan -> throw VmStackNanException()
    }

    override fun minus(other: VmStackNumber): VmStackNumber = when (other) {
        is VmStackInt -> VmStackInt(value - other.value)
        is VmStackTinyInt -> VmStackInt(value - other.value)
        VmStackNan -> throw VmStackNanException()
    }

    override fun times(other: VmStackNumber): VmStackNumber = when (other) {
        is VmStackInt -> VmStackInt(value * other.value)
        is VmStackTinyInt -> VmStackInt(value * other.value)
        VmStackNan -> throw VmStackNanException()
    }

    override fun div(other: VmStackNumber): VmStackNumber = when (other) {
        is VmStackInt -> VmStackInt(value / other.value)
        is VmStackTinyInt -> VmStackInt(value / other.value)
        VmStackNan -> throw VmStackNanException()
    }

    companion object {
        fun tlbConstructor(): TlbConstructor<VmStackInt> = VmStackIntTlbConstructor
    }
}

private object VmStackIntTlbConstructor : TlbConstructor<VmStackInt>(
    schema = "vm_stk_int#0201_ value:int257 = VmStackValue;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStackInt
    ) = cellBuilder {
        storeInt(value.value, 257)
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStackInt = cellSlice {
        val value = loadInt(257)
        VmStackInt(value)
    }
}