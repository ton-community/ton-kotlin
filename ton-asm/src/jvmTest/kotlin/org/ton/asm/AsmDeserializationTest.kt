package org.ton.asm

import org.ton.asm.constant.integer.PUSHINT
import org.ton.asm.stack.basic.XCHG
import org.ton.cell.Cell
import org.ton.cell.CellBuilder
import org.ton.tlb.loadTlb
import org.ton.tlb.parse
import org.ton.tlb.storeTlb
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
        fun checkPushInt(pushInt: PUSHINT) {
            val cell = CellBuilder.createCell {
                storeTlb(PUSHINT, pushInt)
            }
            val actualPushInt = cell.parse(PUSHINT)
            assertEquals(pushInt, actualPushInt)
        }

        checkPushInt(PUSHINT(2))
        checkPushInt(PUSHINT(85))
        checkPushInt(PUSHINT(851))
        checkPushInt(PUSHINT(8514))
        checkPushInt(PUSHINT(85143))
        checkPushInt(PUSHINT(78748))
        checkPushInt(PUSHINT(78748191))
        checkPushInt(PUSHINT(7874819132))
        checkPushInt(PUSHINT(7874819132232))
        checkPushInt(PUSHINT(7874819132232213222))
    }

    @Test
    fun walletv3() {
        Instruction.loadList(Cell("FF0020DD2082014C97BA218201339CBAB19F71B0ED44D0D31FD31F31D70BFFE304E0A4F2608308D71820D31FD31FD31FF82313BBF263ED44D0D31FD31FD3FFD15132BAF2A15144BAF2A204F901541055F910F2A3F8009320D74A96D307D402FB00E8D101A4C8CB1FCB1FCBFFC9ED54").beginParse())
    }
}