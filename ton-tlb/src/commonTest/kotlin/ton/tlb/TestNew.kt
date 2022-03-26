package ton.tlb

import ton.bitstring.BitString
import kotlin.test.Test

class TestNew : InbuiltTypes {

    @Test
    fun test() {
        println(unary())

        val a = BitPrefix(BitString(false), BitString(true, false), BitString(true, true))
        println(a)
    }
}