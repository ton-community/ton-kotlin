import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class PFXDICTCONSTGETJMP (
    val d1: Cell,
    val n: UShort
) : AsmInstruction {

    override fun toString(): String = "$d $n PFXDICTCONSTGETJMP"

    companion object : TlbConstructorProvider<PFXDICTCONSTGETJMP> by PFXDICTCONSTGETJMPTlbConstructor
}

private object PFXDICTCONSTGETJMPTlbConstructor : TlbConstructor<PFXDICTCONSTGETJMP>(
    schema = "asm_pfxdictconsgetjmp#f4ae_ d:^Cell n:uint10 = PFXDICTCONSTGETJMP;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: PFXDICTCONSTGETJMP) {
        cellBuilder.storeRef(value.d)
        cellBuilder.storeUInt(value.n, 10)
    }

    override fun loadTlb(cellSlice: CellSlice): PFXDICTCONSTGETJMP {
        val d = cellSlice.loadRef()
        val n = cellSlice.loadUInt(10).toUShort()
        return PFXDICTCONSTGETJMP(d, n)
    }
}
