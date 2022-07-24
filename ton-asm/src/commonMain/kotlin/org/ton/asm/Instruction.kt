package org.ton.asm

import org.ton.asm.codepage.SETCP
import org.ton.asm.codepage.SETCP0
import org.ton.asm.codepage.SETCPX
import org.ton.asm.constant.integer.*
import org.ton.asm.flow.conditional.*
import org.ton.asm.stack.basic.*
import org.ton.cell.CellSlice
import org.ton.tlb.TlbCombinator
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbCombinatorProvider
import org.ton.tlb.providers.TlbConstructorProvider
import org.ton.tlb.providers.TlbProvider

interface Instruction {
    companion object : TlbCombinatorProvider<Instruction> by InstructionTlbCombinator {
        fun loadList(cellSlice: CellSlice): List<Instruction> {
            val list = ArrayList<Instruction>()
            while (cellSlice.bitsPosition < cellSlice.bits.size) {
                val instruction = loadTlb(cellSlice)
                println(instruction)
                list.add(instruction)
            }
            return list
        }
    }
}

object InstructionTlbCombinator : TlbCombinator<Instruction>() {
    override val constructors: List<TlbConstructor<out Instruction>> = createConstructorList(
        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_21-basic-stack-manipulation-primitives
        NOP, SWAP, XCHG0, XCHG, PUSH, DUP, OVER, POP, DROP, NIP,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_41-integer-and-boolean-constants
        PUSHINT, ZERO, ONE, TWO, TEN, TRUE, PUSHPOW2, PUSHNAN, PUSHPOW2DEC, PUSHNEGPOW2,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_82-conditional-control-flow-primitives
        IFRET, IFNOTRET, IF, IFNOTRET, IFJMP, IFNOTJMP, IFELSE, IFREF, IFNOTREF, IFJMPREF, IFNOTJMPREF, CONDSELCHK,
        IFRETALT, IFNOTRETALT, IFREFELSE, IFELSEREF, IFREFELSEREF, IFBITJMP, IFNBITJMP, IFBITJMPREF, IFNBITJMPREF,

        // https://ton.org/docs/#/smart-contracts/tvm-instructions/instructions?id=_13-codepage-primitives
        SETCP, SETCP0, SETCPX
    )

    private fun createConstructorList(vararg providers: TlbProvider<out Instruction>): List<TlbConstructor<out Instruction>> {
        val list = buildList {
            providers.forEach { provider ->
                when (provider) {
                    is TlbConstructorProvider<out Instruction> -> add(provider.tlbConstructor())
                    is TlbCombinatorProvider<out Instruction> -> addAll(provider.tlbCombinator().constructors)
                }
            }
        }.sortedBy { it.id }
        return list
    }
}