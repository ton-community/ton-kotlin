package org.ton.asm.cell.deserialization

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SCHKREFS : Instruction, TlbConstructorProvider<SCHKREFS> by SCHKREFSTlbConstructor {
    override fun toString(): String = "SCHKREFS"
}

private object SCHKREFSTlbConstructor : TlbConstructor<SCHKREFS>(
    schema = "asm_schkrefs#d742 = SCHKREFS;",
    type = SCHKREFS::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SCHKREFS) {
    }

    override fun loadTlb(cellSlice: CellSlice): SCHKREFS = SCHKREFS
}