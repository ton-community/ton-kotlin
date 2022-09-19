package org.ton.asm.debug

import org.ton.asm.AsmInstruction
import org.ton.bigint.toUByte
import org.ton.bitstring.BitString
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class DEBUGSTR(
    val n: UByte,
    val ssss: BitString
) : AsmInstruction {
    constructor(
        ssss: BitString
    ) : this(
        n = ((ssss.size - 8) / 8).toUByte(),
        ssss = ssss
    )

    init {
        require(ssss.size == n.toInt() * 8 + 8) { "expected ssss.size: $n * 8 + 8, actual: ${ssss.size}" }
    }

    fun toCellSlice(): CellSlice = CellSlice(ssss)

    override fun toString(): String = ssss.toByteArray().decodeToString()

    companion object : TlbConstructorProvider<DEBUGSTR> by DEBUGSTRTlbConstructor
}

private object DEBUGSTRTlbConstructor : TlbConstructor<DEBUGSTR>(
    schema = "asm_debugstr#fef n:(## 4) ssss:((n * 8 + 8) * Bit) = DEBUGSTR;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DEBUGSTR) {
        cellBuilder.storeUInt(value.n, 4)
        cellBuilder.storeBits(value.ssss)
    }

    override fun loadTlb(cellSlice: CellSlice): DEBUGSTR {
        val n = cellSlice.loadUInt(4).toUByte()
        val ssss = cellSlice.loadBits(n.toInt() * 8 + 8)
        return DEBUGSTR(n, ssss)
    }
}
