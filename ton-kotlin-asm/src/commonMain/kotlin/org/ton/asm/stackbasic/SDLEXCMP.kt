package org.ton.asm.stackbasic

import org.ton.asm.AsmInstruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SDLEXCMP : AsmInstruction, TlbConstructorProvider<SDLEXCMP> by SDLEXCMPTlbConstructor {
    override fun toString(): String = "SDLEXCMP"
}

private object SDLEXCMPTlbConstructor : TlbConstructor<SDLEXCMP>(
    schema = "asm_sdlexcmp#c704 = SDLEXCMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SDLEXCMP) {
    }

    override fun loadTlb(cellSlice: CellSlice): SDLEXCMP = SDLEXCMP
}