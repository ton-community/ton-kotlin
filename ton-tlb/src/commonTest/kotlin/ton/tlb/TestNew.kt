package ton.tlb

import kotlinx.serialization.encoding.Decoder
import ton.cell.buildCell
import ton.cell.slice
import kotlin.test.Test

class TestNew {

    @Test
    fun test() {
        val cs = buildCell {
            writeBit(false)  // hml_short$0 {m:#} {n:#} len:(Unary ~n) s:(n * Bit) = HmLabel ~n m;
            writeBits(true, true, true, true, true, true, true, true, false) // Unary 8
            writeUInt(0xfa.toUInt(), 8)
        }.slice()

        val cs2 = buildCell {
            writeBits(true, true, true, false) // Unary 3
        }.slice()


        val result = cs.hmLabel({
            println(it)
        }, value(0))
        println(result)
    }
}