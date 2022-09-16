package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object QLSHIFT : AsmInstruction, TlbConstructorProvider<QLSHIFT> by QLSHIFTTlbConstructor {
    override fun toString(): String = "QLSHIFT"
}

private object QLSHIFTTlbConstructor : TlbConstructor<QLSHIFT>(
    schema = "asm_qlshift#b7ac = QLSHIFT;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: QLSHIFT) {
    }

    override fun loadTlb(cellSlice: CellSlice): QLSHIFT = QLSHIFT
}