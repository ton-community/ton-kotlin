package org.ton.block

import org.ton.bigint.BigInt
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

object VmStackNan : VmStackValue, VmStackNumber {
    override fun toInt(): Int = throw VmStackNanException()
    override fun toLong(): Long = throw VmStackNanException()
    override fun toBigInt(): BigInt = throw VmStackNanException()
    override fun toBoolean(): Boolean = throw VmStackNanException()

    override fun plus(other: VmStackNumber): VmStackNumber = throw VmStackNanException()
    override fun minus(other: VmStackNumber): VmStackNumber = throw VmStackNanException()
    override fun times(other: VmStackNumber): VmStackNumber = throw VmStackNanException()
    override fun div(other: VmStackNumber): VmStackNumber = throw VmStackNanException()

    fun tlbConstructor(): TlbConstructor<VmStackNan> = VmStackValueNanConstructor

    class VmStackNanException : RuntimeException("NaN exception")
}

private object VmStackValueNanConstructor : TlbConstructor<VmStackNan>(
    schema = "vm_stk_nan#02ff = VmStackValue;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder,
        value: VmStackNan
    ) {
    }

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmStackNan = VmStackNan
}
