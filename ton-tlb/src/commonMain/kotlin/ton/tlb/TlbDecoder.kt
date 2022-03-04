package ton.tlb

import ton.bitstring.BitStringReader
import ton.tlb.types.InbuiltTypeFactory
import ton.tlb.types.TonTypeFactory
import ton.tlb.types.TypeCombinator

data class TlbDecoder(
    val cell: Cell,
    val parent: TlbDecoder? = null,
    val reader: BitStringReader = BitStringReader(cell.data),
) : InbuiltTypeFactory, TonTypeFactory {
    var cellRefPointer = 0

    fun decode(typeCombinator: TypeCombinator) = typeCombinator.decode(this)
}
