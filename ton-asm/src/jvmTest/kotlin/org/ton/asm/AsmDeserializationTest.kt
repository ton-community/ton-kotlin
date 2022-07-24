package org.ton.asm

import org.ton.asm.stack.Instruction
import org.ton.asm.stack.basic.XCHG
import org.ton.cell.Cell
import org.ton.tlb.loadTlb
import kotlin.test.Test
import kotlin.test.assertEquals

class AsmDeserializationTest {
    @Test
    fun test() {
        assertEquals(XCHG(5, 8), Cell("1058").beginParse().loadTlb(XCHG))
        assertEquals(XCHG(0, 0xFF), Cell("11FF").beginParse().loadTlb(XCHG))
        assertEquals(XCHG(1, 2), Cell("12").beginParse().loadTlb(XCHG))
    }

    @Test
    fun parse() {
        val instructions = Instruction.loadList(Cell("105811F025313032212022").beginParse())
        instructions.forEach {
            println(it)
        }
    }

    @Test
    fun walletv3() {
        val instructions =
            Instruction.loadList(Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54").beginParse())
    }
}