import org.ton.asm.AsmInstruction
import org.ton.bigint.toUShort
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

data class DICTPUSHCONST (
    val d1: Cell,
    val n: UShort
) : AsmInstruction {

    override fun toString(): String = "$d $n DICTPUSHCONST"

    companion object : TlbConstructorProvider<DICTPUSHCONST> by DICTPUSHCONSTTlbConstructor
}

private object DICTPUSHCONSTTlbConstructor : TlbConstructor<DICTPUSHCONST>(
    schema = "asm_dicptushconst#f4a6_ d:^Cell n:uint10 = DICTPUSHCONST;"
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: DICTPUSHCONST) {
        cellBuilder.storeRef(value.d)
        cellBuilder.storeUInt(value.n, 10)
    }

    override fun loadTlb(cellSlice: CellSlice): DICTPUSHCONST {
        val d = cellSlice.loadRef()
        val n = cellSlice.loadUInt(10).toUShort()
        return DICTPUSHCONST(d, n)
    }
}
