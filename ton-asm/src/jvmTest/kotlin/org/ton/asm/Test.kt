package org.ton.asm

import org.ton.asm.stack.basic.XCHG
import org.ton.cell.Cell
import org.ton.tlb.loadTlb

fun main() {
    val xchg = Cell("1058").beginParse().loadTlb(XCHG)
    println(xchg)
}