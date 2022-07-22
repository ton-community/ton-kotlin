package org.ton.block

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor

@SerialName("vmc_quit_exc")
@Serializable
object VmContQuitExc : VmCont {
    fun tlbConstructor(): TlbConstructor<VmContQuitExc> = VmContQuitExcTlbConstructor
}

private object VmContQuitExcTlbConstructor : TlbConstructor<VmContQuitExc>(
    schema = "vmc_quit_exc\$1001 = VmCont;"
) {
    override fun storeTlb(
        cellBuilder: CellBuilder, value: VmContQuitExc
    ) = Unit

    override fun loadTlb(
        cellSlice: CellSlice
    ): VmContQuitExc = VmContQuitExc
}