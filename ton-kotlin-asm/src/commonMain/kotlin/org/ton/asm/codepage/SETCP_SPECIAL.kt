package org.ton.asm.codepage

import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.bigint.*
import org.ton.tlb.providers.TlbConstructorProvider

data class SETCP_SPECIAL(
    val z: UByte
) {
    init {
        require(z >= 1u) { "expected z >= 1, actual: $z" }
    }

    override fun toString(): String = "${z - 16u} SETCP"

    companion object : TlbConstructorProvider<SETCP_SPECIAL> by SETCP_SPECIALTlbConstructor
}

private object SETCP_SPECIALTlbConstructor : TlbConstructor<SETCP_SPECIAL>(
    schema = "asm_setcp_special#fff z:(## 4) {1 <= z} = SETCP_SPECIAL;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SETCP_SPECIAL) {
        cellBuilder.storeUInt(value.z, 4)
    }

    override fun loadTlb(cellSlice: CellSlice): SETCP_SPECIAL {
        val z = cellSlice.loadUInt(4).toUByte()
        return SETCP_SPECIAL(z)
    }
}
